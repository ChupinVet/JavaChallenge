package br.com.chupinvet.chupinvet.service;

import br.com.chupinvet.chupinvet.dto.DiarioRequestDTO;
import br.com.chupinvet.chupinvet.dto.DiarioResponseDTO;
import br.com.chupinvet.chupinvet.exception.RecursoNaoEncontradoException;
import br.com.chupinvet.chupinvet.model.Diario;
import br.com.chupinvet.chupinvet.model.Pet;
import br.com.chupinvet.chupinvet.repository.DiarioRepository;
import br.com.chupinvet.chupinvet.repository.PetRepository;
import br.com.chupinvet.chupinvet.service.insight.InsightProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiarioService {

    @Autowired
    private DiarioRepository diarioRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private InsightProvider insightProvider;

    @Transactional
    public DiarioResponseDTO cadastrar(DiarioRequestDTO dto) {
        Pet pet = buscarPetOuFalhar(dto.idPet());

        Diario diarioAnterior = diarioRepository
                .findTopByPet_IdPetOrderByDataRegistroDesc(pet.getIdPet())
                .orElse(null);

        Diario diario = new Diario();
        diario.setPet(pet);
        diario.setDataRegistro(dto.dataRegistro());
        diario.setHumor(dto.humor());
        diario.setAlimentacao(dto.alimentacao());
        diario.setConsumoAgua(dto.consumoAgua());
        diario.setComportamento(dto.comportamento());
        diario.setSintomas(dto.sintomas());
        diario.setObservacoes(dto.observacoes());
        diario.setPesoRegistrado(dto.pesoRegistrado());

        // Hoje o provider é no-op (retorna null); na sprint de IA, essa
        // mesma chamada passa a acionar a API externa, sem mudar mais
        // nada aqui.
        diario.setInsightIA(insightProvider.gerarInsight(diario, diarioAnterior));

        Diario diarioSalvo = diarioRepository.save(diario);
        return toResponseDTO(diarioSalvo);
    }

    @Transactional(readOnly = true)
    public Page<DiarioResponseDTO> listarPorPet(Long idPet, Pageable pageable) {
        buscarPetOuFalhar(idPet);
        return diarioRepository.findByPet_IdPetOrderByDataRegistroDesc(idPet, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public DiarioResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarOuFalhar(id));
    }

    @Transactional
    public DiarioResponseDTO atualizar(Long id, DiarioRequestDTO dto) {
        Diario diario = buscarOuFalhar(id);
        Pet pet = buscarPetOuFalhar(dto.idPet());

        diario.setPet(pet);
        diario.setDataRegistro(dto.dataRegistro());
        diario.setHumor(dto.humor());
        diario.setAlimentacao(dto.alimentacao());
        diario.setConsumoAgua(dto.consumoAgua());
        diario.setComportamento(dto.comportamento());
        diario.setSintomas(dto.sintomas());
        diario.setObservacoes(dto.observacoes());
        diario.setPesoRegistrado(dto.pesoRegistrado());
        // insightIA não é reprocessado numa edição manual do registro.

        Diario diarioAtualizado = diarioRepository.save(diario);
        return toResponseDTO(diarioAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        diarioRepository.delete(buscarOuFalhar(id));
    }

    private Diario buscarOuFalhar(Long id) {
        return diarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de diário não encontrado"));
    }

    private Pet buscarPetOuFalhar(Long idPet) {
        return petRepository.findById(idPet)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pet não encontrado"));
    }

    private DiarioResponseDTO toResponseDTO(Diario diario) {
        return new DiarioResponseDTO(
                diario.getIdDiario(),
                diario.getDataRegistro(),
                diario.getHumor(),
                diario.getAlimentacao(),
                diario.getConsumoAgua(),
                diario.getComportamento(),
                diario.getSintomas(),
                diario.getObservacoes(),
                diario.getPesoRegistrado(),
                diario.getInsightIA(),
                diario.getPet().getIdPet(),
                diario.getPet().getNomePet()
        );
    }
}