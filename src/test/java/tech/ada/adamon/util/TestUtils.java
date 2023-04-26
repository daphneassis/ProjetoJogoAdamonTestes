package tech.ada.adamon.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.model.Jogador;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    private TestUtils() {}
    public static List<Adamon> obterAdamons() {
        Adamon adamon = new Adamon();
        Adamon adamon1 = new Adamon();
        Adamon adamon2 = new Adamon();
        Adamon adamon3 = new Adamon();
        Adamon adamon4 = new Adamon();
        Adamon adamon5 = new Adamon();
        return Arrays.asList(adamon, adamon1, adamon2, adamon3, adamon4, adamon5);
    }

    public static Adamon obterAdamon() {
        Adamon adamon = new Adamon();
        adamon.setDefesa(30);
        adamon.setAtaque(50);
        adamon.setVelocidade(30);
        adamon.setInteligencia(40);
        adamon.setPoder(20);
        adamon.setVida(80);
        adamon.setNome("Adachu");
        adamon.setUrlFoto("www.foto.com.br/adachu");
        return adamon;
    }

    public static Jogador obterJogador() {
        Jogador jogador = new Jogador();
        jogador.setId(1L);
        jogador.setNickname("Rodolfo");
        jogador.setPassword("12345678");
        return jogador;
    }

    public static Jogador obterJogador1() {
        Jogador jogador1 = new Jogador();
        jogador1.setId(2L);
        jogador1.setNickname("Felipe");
        jogador1.setPassword("2787877");
        jogador1.setSaldo(BigDecimal.valueOf(100));
        return jogador1;
    }
    public static Jogador obterJogador2() {
        Jogador jogador2 = new Jogador();
        jogador2.setId(3L);
        jogador2.setNickname("Leo");
        jogador2.setPassword("66667777");
        jogador2.setSaldo(BigDecimal.valueOf(500));
        return jogador2;
    }

    public static List<Adamon> obterAdamonsJogador1() {
        Adamon adamon = new Adamon("Morango", 12, 28, 36);
        Adamon adamon1 = new Adamon("Pêssego", 6, 32, 98);
        Adamon adamon2 = new Adamon("Abacaxi", 8, 71, 57);
        Adamon adamon3 = new Adamon("Cereja", 24,42, 89 );
        Adamon adamon4 = new Adamon("Maçã", 13, 99, 68);
        Adamon adamon5 = new Adamon("Banana", 7, 56,88);
        return Arrays.asList(adamon, adamon1, adamon2, adamon3, adamon4, adamon5);
    }

    public static List<Adamon> obterAdamonsJogador2() {
        Adamon adamon = new Adamon("Alface", 3, 67, 51);
        Adamon adamon1 = new Adamon("Cenoura", 9, 25, 30);
        Adamon adamon2 = new Adamon("Beterraba", 5, 77, 20);
        Adamon adamon3 = new Adamon("Abobrinha", 10, 89, 36);
        Adamon adamon4 = new Adamon("Espinafre", 15,39, 99);
        Adamon adamon5 = new Adamon("Cebola", 60, 97, 23);
        return Arrays.asList(adamon, adamon1, adamon2, adamon3, adamon4, adamon5);
    }

    public static String jasonAsString(Object obj){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível converter o objeto");
        }
    }
}
