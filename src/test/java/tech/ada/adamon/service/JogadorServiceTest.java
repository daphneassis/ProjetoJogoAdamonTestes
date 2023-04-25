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
import tech.ada.adamon.model.Jogador;
import tech.ada.adamon.repository.JogadorRepository;
import tech.ada.adamon.util.TestUtils;

import java.math.BigDecimal;
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


    /*
 Testes
 verificar no jogador que ganha se o saldo foi atualizado//
 se quem ganha tem o saldo mesmo valor do inicio do jogo
 verificar no adamon que ganha se a vida foi acrescida
 verificar no adamon que perde se perdeu pontos de vida
 se no caso da vida do adamon zerar, dele ser tirado da lista do jogador
 no caso do atacante ganhar, de ter o ataque aumentado
 no caso do defensor ganhar, de ter a defesa acrescida
 no caso de um adamon morrer a lista tem q encurtar
     */

    @Test
    void deveAtualizarSaldoAposBatalha(){
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());

        //execucao
        jogadorService.batalhar(jogador1, jogador2);

        //verificação
        Assertions.assertTrue(jogador1.getSaldo()!=TestUtils.obterJogador1().getSaldo());

    }
    @Test
    void deveAtualizarSaldoAposBatalhaRandomica(){
        //cenario
        Jogador jogador1 = obterJogador1();
        Jogador jogador2 = obterJogador2();
        jogador1.setAdamons(obterAdamonsJogador1());
        jogador2.setAdamons(obterAdamonsJogador2());

        //execucao
        jogadorService.batalhaAutomatizadaComDoisJogadores(jogador1, jogador2);

        //verificação
        Assertions.assertTrue(jogador1.getSaldo()!=TestUtils.obterJogador1().getSaldo());
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
        Assertions.assertThrows(RuntimeException.class, () -> { jogadorService.comprarAdamon(jogador, adamon); });
    }

    @Test
    void naoDeveConseguirComprarAdamonPoisEquipeEstaCheia() {
        //cenário
        Jogador jogador = obterJogador();
        jogador.setAdamons(TestUtils.obterAdamons()); //equipe cheia
        Adamon adamon = obterAdamon();

        //acao
        Assertions.assertThrows(RuntimeException.class, () -> { jogadorService.comprarAdamon(jogador, adamon); });
    }

}