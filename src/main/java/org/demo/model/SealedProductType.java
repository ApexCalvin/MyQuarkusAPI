package org.demo.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Model class for {@link ProductType} */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Request object for Product Type")
public class SealedProductType {
    
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Schema(description = "Name of the Sealed Product Type", examples = {"Booster Box", "Ultra Premium Collection"})
    String name;
    
    @Schema(description = "MSRP of the Sealed Product Type", examples = {"26.94", "161.64"})
    Double msrp;

    @Schema(description = "Booster Count of the Sealed Product Type", examples = {"6", "36"})
    Integer boosterCount;
}
