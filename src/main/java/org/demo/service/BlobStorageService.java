package org.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.demo.entity.BlobStorage;
import org.demo.entity.BlobStorageId;
import org.demo.entity.Product;
import org.demo.entity.ProductSet;
import org.demo.repository.BlobStorageRepository;
import org.demo.repository.ProductSetRepository;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import lombok.extern.jbosslog.JBossLog;

/**
 * Service class for managing blob storage operations including file uploads, watermarking, and blob retrieval.
 *
 * <p>This service handles various file types and applies watermarks to JPEG and PNG images using company branding
 * information. It supports multiple MIME types and provides automatic file type detection and categorization.
 */
@ApplicationScoped
@Transactional
@JBossLog
public class BlobStorageService {

    // Constants for blob types
    private static final String BLOB_TYPE_WORKLOG = "WORKLOG";
    private static final String BLOB_TYPE_OTHER = "OTHER";

    // Constants for document types
    private static final String DOC_TYPE_SVG = "SVG";
    private static final String DOC_TYPE_PNG = "PNG";
    private static final String DOC_TYPE_JPEG = "JPEG";
    private static final String DOC_TYPE_GIF = "GIF";
    private static final String DOC_TYPE_WEBP = "WEBP";
    private static final String DOC_TYPE_TIFF = "TIFF";
    private static final String DOC_TYPE_PDF = "PDF";
    private static final String DOC_TYPE_DOC = "DOC";
    private static final String DOC_TYPE_DOCX = "DOCX";
    private static final String DOC_TYPE_XLS = "XLS";
    private static final String DOC_TYPE_XLSX = "XLSX";

    // MIME type to document type mapping
    private static final Map<String, String> MIME_TYPE_TO_DOC_TYPE = Map.ofEntries(
            Map.entry("image/svg+xml", DOC_TYPE_SVG),
            Map.entry("image/png", DOC_TYPE_PNG),
            Map.entry("image/jpeg", DOC_TYPE_JPEG),
            Map.entry("image/gif", DOC_TYPE_GIF),
            Map.entry("image/webp", DOC_TYPE_WEBP),
            Map.entry("image/tiff", DOC_TYPE_TIFF),
            Map.entry("video/mp4", "MP4"),
            Map.entry("video/x-msvideo", "AVI"),
            Map.entry("video/x-matroska", "WEBM"),
            Map.entry("video/webm", "WEBM"),
            Map.entry("video/quicktime", "MOV"),
            Map.entry("video/mpeg", "MPEG"),
            Map.entry("video/ogg", "OGG"),
            Map.entry("video/3gpp", "3GPP"),
            Map.entry("video/x-flv", "FLV"),
            Map.entry("video/x-ms-wmv", "WMV"),
            Map.entry("video/mp4; codecs=\"avc1.42E01E\"", "H264"),
            Map.entry("video/mp4; codecs=\"avc1.64001F\"", "H264"),
            Map.entry("video/mp4; codecs=\"avc1\"", "H264"),
            Map.entry("video/avc", "H264"),
            Map.entry("video/h264", "H264"),
            Map.entry("video/mp4; codecs=\"hvc1\"", "H265"),
            Map.entry("video/mp4; codecs=\"hev1\"", "H265"),
            Map.entry("video/hevc", "H265"),
            Map.entry("video/h265", "H265"),
            Map.entry("image/heif", "HEIF"),
            Map.entry("image/heic", "HEIF"),
            Map.entry("application/pdf", DOC_TYPE_PDF),
            Map.entry("application/msword", DOC_TYPE_DOC),
            Map.entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", DOC_TYPE_DOCX),
            Map.entry("application/vnd.ms-excel", DOC_TYPE_XLS),
            Map.entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", DOC_TYPE_XLSX));

    @Inject
    ProductSetRepository productSetRepository;

    @Inject
    BlobStorageRepository blobStorageRepository;

    // @Inject
    // CompanyConfig companyConfig;

    // @Inject
    // ApplicationService applicationService;

    // @Inject
    // ImageService imageService;

    // @Inject
    // ProfileMasterRepository profileMasterRepository;

    /**
     * Uploads a file attachment to an existing entity.
     *
     * <p>This method handles file uploads for entity, automatically managing blob storage allocation and file
     * metadata. The method uses pessimistic locking to prevent concurrent modification issues during the upload
     * process.
     *
     * <p>The upload process includes:
     *
     * <ul>
     *   <li>Verification that the entity exists
     *   <li>Automatic blob number allocation if is not already assigned
     *   <li>File storage with proper metadata
     *   <li>Association with the entity record
     * </ul>
     *
     * @param id The unique entity identifier. Must not be null or empty.
     * @param fileUploadRequest The file upload request containing the file data and metadata. Must not be null.
     * @return The stored blob storage entity containing the file information
     * @throws IllegalArgumentException if the ID or file upload request is null
     * @throws NotFoundException if the entity with the specified ID does not exist
     * @see BlobStorage
     * @see FileUpload
     */
    public BlobStorage uploadAttachment(String id, FileUpload fileUploadRequest) {
        log.infof("Uploading attachment to entity: %s", id);
        ProductSet productSet = productSetRepository.findExistingById(id, LockModeType.PESSIMISTIC_WRITE);
        Long blobNumber = productSet.getBlobNo();
        if (blobNumber == null || blobNumber == 0L) {
            blobNumber = getNextBlobNumber();
            productSet.setBlobNo(blobNumber);
            productSetRepository.merge(productSet); 
        }
        return saveFileUpload(blobNumber, fileUploadRequest, ProductSet.TABLE_NAME);
    }
    
    /**
     * Gets the next available blob number from the application configuration.
     *
     * @return the next blob number
     * @throws RuntimeException if unable to retrieve the configuration number
     */
    public Long getNextBlobNumber() {
        // TODO: write native query to get and increment blob number in a single transaction to avoid concurrency issues
        return 1L;
        //applicationService.getConfigNumber("BLOB");
    }

    /**
     * Saves a file upload to blob storage with appropriate metadata and processing.
     *
     * <p>This method performs the following operations:
     *
     * <ul>
     *   <li>Determines the next available line number for the blob
     *   <li>Sets appropriate blob type and expiration based on the source table
     *   <li>Detects and sets the document type based on MIME type
     *   <li>Applies watermark to JPEG images
     *   <li>Persists the blob storage record
     * </ul>
     *
     * @param blobNumber the blob number to associate with the file
     * @param upload the file upload containing the file data and metadata
     * @param fromTable the source table identifier for the upload
     * @return the created blob storage record
     * @throws IllegalArgumentException if any parameter is null or invalid
     * @throws BadRequestException if there's an error reading or processing the file
     * @throws RuntimeException if there's an error persisting the record
     */
    public BlobStorage saveFileUpload(Long blobNumber, FileUpload upload, String fromTable) {
        // Validate input parameters
        Objects.requireNonNull(blobNumber, "Blob number cannot be null");
        Objects.requireNonNull(upload, "File upload cannot be null");
        Objects.requireNonNull(fromTable, "From table cannot be null");

        if (StringUtils.isBlank(upload.fileName())) {
            throw new IllegalArgumentException("File name cannot be blank");
        }

        log.infof("Saving file upload: %s", upload.fileName());

        try {
            // Find the next available line number for this blob
            final long nextLine = blobStorageRepository.getNextBlobLine(blobNumber);
            final BlobStorage blobStorage = createBlobStorage(blobNumber, nextLine, upload, fromTable);

            // Read and process file content
            byte[] fileBytes = Files.readAllBytes(upload.filePath());
            // if (traxConfig.watermarkEnabled()
            //         && (DOC_TYPE_JPEG.equals(blobStorage.getDocType())
            //                 || DOC_TYPE_PNG.equals(blobStorage.getDocType()))) {
            //     fileBytes = imageService.addWaterMark(fileBytes);
            // }
            blobStorage.setBlobItem(fileBytes);

            // Persist the record
            blobStorageRepository.persist(blobStorage);

            log.infof(
                    "Successfully saved file upload: %s with blob number: %d, line: %d",
                    upload.fileName(), blobNumber, nextLine);

            return blobStorage;

        } catch (IOException e) {
            log.errorf("Error reading file: %s", upload.fileName(), e);
            throw new BadRequestException("Error reading file: " + upload.fileName(), e);
        }
    }

    /**
     * Creates a new BlobStorage entity with the provided parameters.
     *
     * @param blobNumber the blob number
     * @param nextLine the line number
     * @param upload the file upload
     * @param fromTable the source table
     * @return the configured BlobStorage entity
     */
    private BlobStorage createBlobStorage(Long blobNumber, long nextLine, FileUpload upload, String fromTable) {
        final BlobStorage blobStorage = new BlobStorage();
        blobStorage.setId(new BlobStorageId(blobNumber, nextLine));
        blobStorage.setBlobName(upload.fileName());
        blobStorage.setFromTable(fromTable);
        blobStorage.setMimeType(upload.contentType());
 
        // Set blob type and expiration based on source table
        setBlobTypeAndExpiration(blobStorage, fromTable);

        // Set document type based on MIME type
        setDocumentType(blobStorage, upload.contentType());

        return blobStorage;
    }

    /**
     * Sets the blob type and expiration date based on the source table.
     *
     * @param blobStorage the blob storage entity to configure
     * @param fromTable the source table identifier
     */
    private void setBlobTypeAndExpiration(BlobStorage blobStorage, String fromTable) {
        switch (fromTable) {
            case "WORKLOG":
                blobStorage.setBlobType(BLOB_TYPE_WORKLOG);
                //blobStorage.setExpireDate(ZonedDateTime.now().plus(traxConfig.worklogStorageDuration()));
                break;
            default:
                blobStorage.setBlobType(BLOB_TYPE_OTHER);
            // No expiration for other types
        }
    }

    /**
     * Sets the document type based on the MIME type.
     *
     * @param blobStorage the blob storage entity to configure
     * @param contentType the MIME content type
     */
    private void setDocumentType(BlobStorage blobStorage, String contentType) {
        String docType = MIME_TYPE_TO_DOC_TYPE.get(contentType);
        if (docType == null) {
            // Fallback for unknown MIME types
            docType = StringUtils.truncate(contentType, 10);
        }
        blobStorage.setDocType(docType);
    }

    /**
     * Retrieves a specific file attachment from a worklog.
     *
     * <p>This method fetches a file attachment associated with a worklog and validates that the attachment exists and
     * contains actual file data.
     *
     * <p>The method performs the following validations:
     *
     * <ul>
     *   <li>Verifies the worklog exists
     *   <li>Checks that the attachment exists for the given attachment ID
     *   <li>Validates that the attachment contains actual file data
     * </ul>
     *
     * @param id The unique worklog identifier. Must not be null or empty.
     * @param attachmentId The unique attachment identifier within the worklog's blob storage. Must not be null.
     * @return The blob storage entity containing the attachment data and metadata
     * @throws IllegalArgumentException if the ID or attachment ID is null
     * @throws NotFoundException if the worklog or attachment does not exist, or if the attachment is empty
     * @see BlobStorage
     * @see BlobStorageId
     */
    public BlobStorage getAttachment(String id, Long attachmentId) {
        final ProductSet productSet = productSetRepository.findExistingById(id);
        final BlobStorage attachment = findById(new BlobStorageId(productSet.getBlobNo(), attachmentId));
        if (attachment == null) {
            throw new NotFoundException("ProductSet attachment " + attachmentId + " not found");
        }
        if (attachment.getBlobItem() == null) {
            throw new NotFoundException("ProductSet attachment " + attachmentId + " is empty");
        }
        return attachment;
    }

    /**
     * Finds a blob storage record by its composite ID.
     *
     * @param id the composite ID containing blob number and line
     * @return the blob storage record, or null if not found
     * @throws IllegalArgumentException if id is null
     */
    public BlobStorage findById(BlobStorageId id) {
        Objects.requireNonNull(id, "BlobStorageId cannot be null");
        return blobStorageRepository.findById(id);
    }

    // /**
    //  * Finds all blob storage records by blob number.
    //  *
    //  * @param blobNo the blob number to search for
    //  * @return a list of blob storage records, empty list if none found
    //  * @throws IllegalArgumentException if blobNo is null
    //  */
    // public List<BlobStorage> findByBlobNo(Long blobNo) {
    //     Objects.requireNonNull(blobNo, "Blob number cannot be null");
    //     return blobStorageRepository.findByBlobNumber(blobNo);
    // }

}
