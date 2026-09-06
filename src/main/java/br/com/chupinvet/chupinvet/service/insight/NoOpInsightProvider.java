package br.com.chupinvet.chupinvet.service.insight;

import br.com.chupinvet.chupinvet.model.Diario;
import org.springframework.stereotype.Service;

/**
 * Implementação provisória: não gera nenhum insight. Fica registrada como
 * @Service para que o Spring já injete algo no DiarioService hoje; quando
 * a API de IA externa for integrada, basta criar outra implementação de
 * InsightProvider (ex.: IaApiInsightProvider) e marcar esta como
 * @Primary=false ou removê-la.
 */
@Service
public class NoOpInsightProvider implements InsightProvider {

    @Override
    public String gerarInsight(Diario diarioAtual, Diario diarioAnterior) {
        return null;
    }
}