package br.com.chupinvet.chupinvet.service.insight;

import br.com.chupinvet.chupinvet.model.Diario;

/**
 * Ponto de extensão para a geração do campo ds_insight_ia.
 *
 * Nesta sprint, o insight NÃO é gerado por nós — será responsabilidade de
 * uma API de IA externa, a ser integrada na próxima sprint. A ideia deste
 * Strategy é permitir trocar a implementação (de "não faz nada" para
 * "chama a API de IA") sem alterar o DiarioService.
 *
 * @param diarioAtual    o registro que está sendo salvo agora
 * @param diarioAnterior o registro mais recente anterior deste mesmo pet,
 *                       ou null se for o primeiro registro
 */
public interface InsightProvider {

    String gerarInsight(Diario diarioAtual, Diario diarioAnterior);
}