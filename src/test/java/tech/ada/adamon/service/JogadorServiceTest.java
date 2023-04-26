package tech.ada.adamon.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.model.Inventario;
import tech.ada.adamon.model.Jogador;
import tech.ada.adamon.repository.JogadorRepository;
import tech.ada.adamon.util.TestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static tech.ada.adamon.util.TestUtils.*;

@ExtendWith(MockitoExtension.class)
class JogadorServiceTest {

    @InjectMocks
    JogadorService jogadorService;

    @Mock
    private JogadorRepository repository;


    @Test
    void deveAtualizarSaldoAposBatalha() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        BigDecimal saldoJogador1AntesBatalha = jogador1.getSaldo();
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertNotEquals(saldoJogador1AntesBatalha, jogador1.getSaldo());
    }

    @Test
    void deveMudarTamanhoListaAdamonsAposBatalha() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        int tamanhoListaAdamonsAntesBatalha = TestUtils.obterAdamonsJogador1().size();
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertNotEquals(jogador1.getAdamons().size(), tamanhoListaAdamonsAntesBatalha);
    }

    @Test
    void deveAumentarVidaEDefesaCasoDefensorGanhe() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        Adamon adamonTeste = jogador2.getAdamons().get(0);
        int adamonVidaAntesDaBatalha = adamonTeste.getVida();
        int adamonDefesaAntesDaBatalha = adamonTeste.getDefesa();
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertTrue(adamonTeste.getVida() > adamonVidaAntesDaBatalha);
        Assertions.assertTrue(adamonTeste.getDefesa() > adamonDefesaAntesDaBatalha);
    }

    @Test
    void deveAumentarVidaEAtaqueCasoAtacanteGanhe() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        Adamon adamonTeste = jogador1.getAdamons().get(2);
        int adamonVidaAntesDaBatalha = adamonTeste.getVida();
        int adamonAtaqueAntesDaBatalha = adamonTeste.getAtaque();
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertTrue(adamonTeste.getVida() > adamonVidaAntesDaBatalha);
        Assertions.assertTrue(adamonTeste.getAtaque() > adamonAtaqueAntesDaBatalha);
    }

    @Test
    void devePerderPontosDeVidaQuemPerde() {
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        Adamon adamonTeste = jogador1.getAdamons().get(0);
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertFalse(adamonTeste.getVida() >= 0);
    }

    @Test
    void deveSerRetiradoDaListaQuemFicaComVidaMenorIgualAZero() {
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        Adamon adamonTeste = jogador1.getAdamons().get(0);
        //execucao
        jogadorService.batalhar(jogador1, jogador2);
        //verificação
        Assertions.assertNotSame(adamonTeste, jogador1.getAdamons().get(0));
    }

    @Test
    void naoDeveConseguirJogarComEquipeIncompleta() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        Adamon adamonASerDeletado = jogador1.getAdamons().get(0);
        List<Adamon> listaComAdamonDeletado = new ArrayList<>(jogador1.getAdamons());
        listaComAdamonDeletado.remove(adamonASerDeletado);
        jogador1.setAdamons(listaComAdamonDeletado);
        //ação e verificacao
        Assertions.assertThrows(RuntimeException.class, () -> {
            jogadorService.batalhar(jogador1, jogador2);
        });
    }

    @Test
    void naoDeveConseguirJogarComEquipeVazia() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador2.setAdamons(obterAdamonsJogador2());
        //ação e verificacao
        Assertions.assertThrows(RuntimeException.class, () -> {
            jogadorService.batalhar(jogador1, jogador2);
        });
    }

    @Test
    void deveAtualizarSaldoAposBatalhaRandomica() {
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());
        BigDecimal saldoJogador1AntesBatalha = jogador1.getSaldo();
        //execucao
        jogadorService.batalhaAutomatizadaComDoisJogadores(jogador1, jogador2);
        //verificação
        Assertions.assertNotEquals(saldoJogador1AntesBatalha, jogador1.getSaldo());
    }


    @Test
    void deveConseguirComprarAdamon() {
        //cenário
        Jogador jogador = obterJogador();
        Adamon adamon = obterAdamon();
        Mockito.when(repository.findById(jogador.getId())).thenReturn(Optional.of(jogador));
        //acao
        jogadorService.comprarAdamon(jogador, adamon);

        //verificação
        assertFalse(jogador.getAdamons().isEmpty());
        Mockito.verify(repository).save(jogador);
    }

    @Test
    void naoDeveConseguirComprarAdamonNaoPossuiSaldo() {
        //cenário
        Jogador jogador = obterJogador();
        jogador.setSaldo(BigDecimal.valueOf(0)); //saldo 0
        Adamon adamon = obterAdamon();

        //acao e verificação
        Assertions.assertThrows(RuntimeException.class, () -> {
            jogadorService.comprarAdamon(jogador, adamon);
        });
    }

    @Test
    void naoDeveConseguirComprarAdamonPoisEquipeEstaCheia() {
        //cenário
        Jogador jogador = obterJogador();
        jogador.setAdamons(TestUtils.obterAdamons()); //equipe cheia
        Adamon adamon = obterAdamon();

        //acao
        Assertions.assertThrows(RuntimeException.class, () -> {
            jogadorService.comprarAdamon(jogador, adamon);
        });
    }

}