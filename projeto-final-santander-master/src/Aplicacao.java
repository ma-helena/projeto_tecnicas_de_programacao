import dominio.PosicaoTabela;
import dominio.Resultado;
import impl.CampeonatoBrasileiroImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Aplicacao {

    public static void main(String[] args) throws IOException {


        CampeonatoBrasileiroImpl resultados =
                new CampeonatoBrasileiroImpl( (jogo) -> jogo.data().data().getYear() == 2020 ||     jogo.data().data().getYear() == 2021);

        imprimirEstatisticas(resultados);

        imprimirTabela(resultados.getTabelaOrdenada());

    }

    private static void imprimirEstatisticas(CampeonatoBrasileiroImpl brasileirao) {
        IntSummaryStatistics statistics = brasileirao.getEstatisticasPorJogo();

        System.out.println("Estatisticas (Total de gols) - " + statistics.getSum());
        System.out.println("Estatisticas (Total de jogos) - " + statistics.getCount());
        System.out.println("Estatisticas (Media de gols) - " + statistics.getAverage());

        Map.Entry<String, Long> placarMaisRepetido = (Map.Entry<String, Long>) brasileirao.getPlacarMaisRepetido();

        System.out.println("Estatisticas (Placar mais repetido) - "
                + placarMaisRepetido.getKey() + " (" +placarMaisRepetido.getValue() + " jogo(s))");

        Map.Entry<String, Long> placarMenosRepetido = brasileirao.getPlacarMenosRepetido();

        System.out.println("Estatisticas (Placar menos repetido) - "
                + placarMenosRepetido.getKey() + " (" +placarMenosRepetido.getValue() + " jogo(s))");

        Long jogosCom3OuMaisGols = brasileirao.getTotalJogosCom3OuMaisGols();
        Long jogosComMenosDe3Gols = brasileirao.getTotalJogosComMenosDe3Gols();

        System.out.println("Estatisticas (3 ou mais gols) - " + jogosCom3OuMaisGols);
        System.out.println("Estatisticas (-3 gols) - " + jogosComMenosDe3Gols);

        Long totalVitoriasEmCasa = brasileirao.getTotalVitoriasEmCasa();
        Long vitoriasForaDeCasa = brasileirao.getTotalVitoriasForaDeCasa();
        Long empates = brasileirao.getTotalEmpates();

        System.out.println("Estatisticas (Vitorias Fora de casa) - " + vitoriasForaDeCasa);
        System.out.println("Estatisticas (Vitorias Em casa) - " + totalVitoriasEmCasa);
        System.out.println("Estatisticas (Empates) - " + empates);
    }

    public static void imprimirTabela(List<PosicaoTabela> posicoes) {
        System.out.println();
        System.out.println("## TABELA CAMPEONATO BRASILEIRO: ##");
        int colocacao = 1;
        for (PosicaoTabela posicao : posicoes) {
            System.out.println(colocacao +". " + posicao);
            colocacao++;
        }

        System.out.println();
        System.out.println();
    }
}