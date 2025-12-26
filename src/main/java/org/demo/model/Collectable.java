package org.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Model class for Product */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Request object for Product")
public class Collectable {
    
    @Id
    @Size(max = 36)
    @Schema(description = "Unique identifier", examples = "abcd-1234-efgh-5678")
    private String id;

    @NotBlank(message = "Set is required") 
    @Size(max = 50, message = "Set must be less than 50 characters") 
    @Schema(description = "Set of collectable", examples = {"Steam Siege", "Roaring Skies", "Ancient Origins"})
    private String setName;

    @NotBlank(message = "Type is required") 
    @Size(max = 50, message = "Type must be less than 50 characters") 
    @Schema(description = "Type of collectable", examples = {"Booster Box", "Elite Trainer Box", "Booster Bundle"})
    private String type;

    @NotEmpty(message = "Release Date is required") 
    @Schema(description = "Release date of collectable")
    private LocalDate releaseDate;

    @NotEmpty(message = "Purchase Date is required") 
    @Schema(description = "Purchase date of collectable")
    private LocalDate purchaseDate;

    @NotNull(message = "Release price is required") 
    @Schema(description = "Release price of collectable")
    private BigDecimal releasePrice;

    @NotNull(message = "Purchase price is required") 
    @Schema(description = "Purchase price of collectable")
    private BigDecimal purchasePrice;

    @NotNull(message = "Special Edition is required")
    @Schema(description = "If the collectable is a special edition")
    private boolean specialEdition;
}
