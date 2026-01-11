package org.demo.model;

import java.time.LocalDate;

import org.demo.entity.ProductSet;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Model class for {@link ProductSet} */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Request object for Product Set")
public class EnglishSet {

    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must be less than 10 characters")
    @Schema(description = "Code of the English Set", examples = {"MEW", "ABC", "DEF"})
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Schema(description = "Name of the English Set", examples = {"Plasma Freeze", "Evolving Skies", "Steam Siege"})
    private String name;

    @NotNull(message = "Specialty is required")
    @Schema(description = "If the English Set is a specialty set", examples = {"true", "false"})
    private boolean specialty;

    @Schema(description = "Release date of the English Set", examples = {"2024-01-15"})
    private LocalDate releaseDate;

    @Size(max = 255, message = "Name must be less than 255 characters")
    @Schema(description = "Series of the English Set", examples = {"Sun & Moon", "Sword & Shield", "Scarlet & Violet"})
    private String series;
}
