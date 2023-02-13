package impl;

import dominio.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

public class CampeonatoBrasileiroImpl {

    private List<Jogo> brasileirao;
    private List<Jogo> jogos;
    private Predicate<Jogo> filtro;

    public CampeonatoBrasileiroImpl(Predicate<Jogo> filtro) throws IOException {
        this.jogos = lerArquivo();
        this.filtro = filtro;
        this.brasileirao = jogos.stream()
                .filter(filtro)
                .toList();

    }

    public List<Jogo> lerArquivo() throws IOException {

        String path = "C:\\Users\\helen\\OneDrive\\Área de Trabalho\\Santander Coders\\projeto_tecnicas_de_programacao\\projeto-final-santander-master\\campeonato-brasileiro.csv";
        List<Jogo> listaJogo = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            line = br.readLine();
            while (line != null) {
                String[] dados = line.split(";");
                Integer rodada = Integer.parseInt(dados[0]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataJogo = LocalDate.parse(dados[1], formatter);
                LocalTime horaJogo = null;
                if (!"".equals(dados[2]) && dados[2] != null) {
                    String horarioFormatter = dados[2].replaceAll("h", ":");
                    horaJogo = LocalTime.parse(horarioFormatter);
                }
                DayOfWeek diaJogo = dataJogo.getDayOfWeek();
                DataDoJogo dataCompletaJogo = new DataDoJogo(dataJogo, horaJogo, diaJogo);
                Time mandante = new Time(dados[4]);
                Time visitante = new Time(dados[5]);
                Time vencedor = new Time(dados[6]);
                String arena = dados[7];
                Integer mandantePlacar = Integer.parseInt(dados[8]);
                Integer visitantePlacar = Integer.parseInt(dados[9]);
                String estadoMandante = dados[10];
                String estadoVisitante = dados[11];
                String estadoVencedor = dados[12];
                Jogo jogo = new Jogo(rodada, dataCompletaJogo, mandante, visitante, vencedor, arena, mandantePlacar, visitantePlacar, estadoMandante, estadoVisitante, estadoVencedor);
                listaJogo.add(jogo);
                line = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return listaJogo;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        IntStream estatisticasCampeonato = todosOsJogos().stream().mapToInt(jogo -> jogo.visitantePlacar() + jogo.mandantePlacar());
        return estatisticasCampeonato.summaryStatistics();
    }



    public List<Jogo> todosOsJogos() {
        return brasileirao;
    }

    public Long getTotalVitoriasEmCasa() {
        return brasileirao
                .stream()
                .filter(jogo -> jogo.mandante().equals(jogo.vencedor()))
                .count();
    }

    public Long getTotalVitoriasForaDeCasa() {
        return brasileirao
                .stream().filter(jogo -> jogo.visitante().equals(jogo.vencedor()))
                .count();
    }

    public Long getTotalEmpates() {
        return brasileirao
                .stream().filter(jogo -> jogo.vencedor().toString().equals("-"))
                .count();
    }

    public Long getTotalJogosComMenosDe3Gols() {
        int gols = 3;
        return brasileirao.stream().filter(jogo -> jogo.mandantePlacar() + jogo.visitantePlacar() < gols).count();
    }

    public Long getTotalJogosCom3OuMaisGols() {
        int gols = 3;
        return brasileirao.stream().filter(jogo -> jogo.mandantePlacar() + jogo.visitantePlacar() >= gols).count();
    }

    public List<String> getTodosOsPlacares() {
        return brasileirao.stream().map(jogo -> jogo.mandantePlacar() + "-" + jogo.visitantePlacar()).toList();
    }


    public long getTodosOsgols() {
        return brasileirao.stream().map(jogo -> jogo.mandantePlacar() + jogo.visitantePlacar()).count();
    }

    public Map.Entry<String, Long> getPlacarMaisRepetido() {
        List<String> listaDePlacarMais = getTodosOsPlacares();

        return listaDePlacarMais
                .stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).get();
    }

    public Map.Entry<String, Long> getPlacarMenosRepetido() {
        List<String> listaDePlacarMenos = getTodosOsPlacares();

        return listaDePlacarMenos.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().min(Map.Entry.comparingByValue()).get();
    }

    public List<Time> getTodosOsTimes() {

        return todosOsJogos().stream().map(Jogo::mandante).distinct().toList();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {

        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> timesMandantes = todosOsJogos().stream().filter(jogo -> jogo.mandante().equals(time)).toList();
            Map<Time, List<Jogo>> jogosTimesMandantes = new HashMap<>();
            jogosTimesMandantes.put(time, timesMandantes);
            for (Map.Entry<Time, List<Jogo>> entry : jogosTimesMandantes.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            return jogosTimesMandantes;
        }


        return getTodosOsJogosPorTimeComoMandantes();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> timesVisitantes = todosOsJogos().stream().filter(jogo -> jogo.visitante().equals(time)).toList();
            Map<Time, List<Jogo>> jogosTimesVisitantes = new HashMap<>();
            jogosTimesVisitantes.put(time, timesVisitantes);
            for (Map.Entry<Time, List<Jogo>> entry : jogosTimesVisitantes.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            return jogosTimesVisitantes;
        }

        return getTodosOsJogosPorTimeComoVisitante();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        Map<Time, List<Jogo>> jogosTime = new HashMap<>();
        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> todosOsJogosTime = todosOsJogos().stream().filter(jogo -> jogo.visitante().equals(time) || jogo.mandante().equals(time)).toList();
            jogosTime.put(time, todosOsJogosTime);

            }
        return jogosTime ;
        }

    public Map<Time, Long> getTotalDeGolsPorTime() {
        Map<Time, Long> mapGolsFavoraveis = new HashMap<>();
        for (Map.Entry<Time, List<Jogo>> timeEntry: getTodosOsJogosPorTime().entrySet()){
            Time time = timeEntry.getKey();
            var golsMandante = timeEntry.getValue().stream().filter(jogo -> jogo.mandante().equals(time)).mapToInt(Jogo::mandantePlacar).summaryStatistics();
            var golsVisitante = timeEntry.getValue().stream().filter(jogo -> jogo.visitante().equals(time)).mapToInt(Jogo::visitantePlacar).summaryStatistics();
            long totalGols = golsMandante.getSum() + golsVisitante.getSum();
            mapGolsFavoraveis.put(time,totalGols);
        }

        return mapGolsFavoraveis;
    }

    public Map<Time, Long> getGolsSofridosPorTime() {
        Map <Time, Long> mapGolsSofridos = new HashMap<>();
        for (Map.Entry<Time, List<Jogo>> timeEntry: getTodosOsJogosPorTime().entrySet()){
            Time time = timeEntry.getKey();
            var golsSofridosMandante = timeEntry.getValue().stream().filter(jogo -> jogo.mandante().equals(time)).mapToInt(Jogo::visitantePlacar).summaryStatistics();
            var golsSofridosVisitante = timeEntry.getValue().stream().filter(jogo -> jogo.visitante().equals(time)).mapToInt(Jogo::mandantePlacar).summaryStatistics();
            long golsSofridos = golsSofridosMandante.getSum() + golsSofridosVisitante.getSum();
            mapGolsSofridos.put(time,golsSofridos);


    }
        return mapGolsSofridos;}

        public List<PosicaoTabela> getTabela(){
            List<PosicaoTabela> posicaoTabela = new ArrayList<>();
            for (Map.Entry<Time, List<Jogo>> timeListEntry : getTodosOsJogosPorTime().entrySet()){
                Time time = timeListEntry.getKey();
                long vitorias = timeListEntry.getValue().stream().filter(jogo -> jogo.vencedor().equals(time)).count();
                long empates = timeListEntry.getValue().stream().filter(jogo -> jogo.vencedor().toString().equals("-")).count();
                long derrotas = timeListEntry.getValue().size() - vitorias - empates;
                long golsSofridos = getGolsSofridosPorTime().get(time);
                long golsPositivos = getTotalDeGolsPorTime().get(time);
                long pontos =(3*vitorias + empates);
                long saldoDeGols = golsPositivos - golsSofridos;
                long jogos =  timeListEntry.getValue().size();
                PosicaoTabela posicaoTabelaFinal = new PosicaoTabela(time, pontos, vitorias, derrotas, empates, golsPositivos, golsSofridos,saldoDeGols, jogos);
                posicaoTabela.add(posicaoTabelaFinal);


            }
            return  posicaoTabela;


        }

        public List<PosicaoTabela> getTabelaOrdenada(){
        return getTabela().stream().sorted().toList();
        }




}