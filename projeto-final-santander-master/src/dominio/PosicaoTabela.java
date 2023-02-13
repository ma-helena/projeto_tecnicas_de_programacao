package dominio;

public record PosicaoTabela(Time time,
                            Long pontos,
                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) implements Comparable<PosicaoTabela> {

    @Override
    public String toString() {
        return  time +
                ", pontos=" + pontos +
                ", vitorias=" + vitorias +
                ", derrotas=" + derrotas +
                ", empates=" + empates +
                ", golsPositivos=" + golsPositivos +
                ", golsSofridos=" + golsSofridos +
                ", saldoDeGols=" + saldoDeGols +
                ", jogos=" + jogos +
                '}';
    }

    @Override
    public int compareTo(PosicaoTabela posicao) {
        if (posicao.pontos() != pontos()){
            return Long.compare(posicao.pontos(),pontos());
        }
        else if (posicao.vitorias() != vitorias()){
            return Long.compare(posicao.vitorias(),vitorias());
        }
        return Long.compare(posicao.saldoDeGols(),saldoDeGols());
    }
}
