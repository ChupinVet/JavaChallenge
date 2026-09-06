package br.com.chupinvet.chupinvet.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados retornados de um registro de diário")
public record DiarioResponseDTO(

        @Schema(example = "1")
        Long idDiario,

        @Schema(example = "2026-06-15")
        LocalDate dataRegistro,

        @Schema(example = "Feliz")
        String humor,

        @Schema(example = "Normal")
        String alimentacao,

        @Schema(example = "Bebeu bem")
        String consumoAgua,

        String comportamento,

        String sintomas,

        String observacoes,

        @Schema(example = "28.5")
        Double pesoRegistrado,

        @Schema(description = "Insight gerado por IA (preenchido em sprint futura, pode vir nulo)")
        String insightIA,

        @Schema(example = "1")
        Long idPet,

        @Schema(example = "Nasus")
        String nomePet

) {
}