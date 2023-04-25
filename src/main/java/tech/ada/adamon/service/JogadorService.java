package tech.ada.adamon.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.adamon.dto.SalvarJogadorDTO;
import tech.ada.adamon.dto.converter.JogadorDtoConverter;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.model.Jogador;
import tech.ada.adamon.repository.JogadorRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class JogadorService {

    @Autowired
    private JogadorRepository jogadorRepository;

    /*
    Criar um método batalhar que recebe dois jogadores, e este método será responsável pela
    lógica de uma batalha entre duas equipes de Adamons. A lógica da batalha fica a sua escolha,
    um jogador será vitorioso o adversário não possuir mais adamons vivos (vida > 0);
     */


    public void batalhar(Jogador jogador1, Jogador jogador2) {
        List<Adamon> equipeAdamonJogadorUm = jogador1.getAdamons();
        List<Adamon> equipeAdamonJogadorDois = jogador2.getAdamons();
        Jogador atacante = jogador1;
        Jogador defensor = jogador2;
        for(int i=0;i<6;i++) {
            while ((equipeAdamonJogadorUm.size()> 0) && (equipeAdamonJogadorDois.size()> 0)) {
                Adamon adamonAtacante = equipeAdamonJogadorUm.get(i);
                Adamon adamonDefensor = equipeAdamonJogadorDois.get(i);
                int diferencaAtaque = adamonAtacante.getAtaque() - adamonDefensor.getDefesa();
                if (diferencaAtaque > 0) {
                    cenarioAtacanteGanha(atacante, adamonAtacante);
                    descrescimoVidaPerdedor(adamonDefensor);
                    if (adamonDefensor.getVida() <= 0) {
                       List<Adamon> listaAtualAdamonsDefensor = new ArrayList<>(defensor.getAdamons());
                       listaAtualAdamonsDefensor.remove(adamonDefensor);
                       defensor.setAdamons(listaAtualAdamonsDefensor);
                        System.out.println("O adamon "+ adamonDefensor.getNome()+ "morreu");
                    }
                } else {
                    cenarioDefensorGanha(defensor, adamonDefensor);
                    descrescimoVidaPerdedor(adamonAtacante);
                    if (adamonAtacante.getVida() <= 0) {
                       List<Adamon> listaAtualAdamonsAtacante = new ArrayList<>(atacante.getAdamons());
                       listaAtualAdamonsAtacante.remove(adamonAtacante);
                       atacante.setAdamons(listaAtualAdamonsAtacante);
                        System.out.println("O adamon "+ adamonAtacante.getNome()+ "morreu");
                    }
                }
            }
        }
    }
    /*
    Regras da Batalha Dentre Dois Jogadores De Forma Automatizada:

    A batalha vai durar tantos rounds quanto preciso até que uma das listas esteja vazia
    Metodo aleatorio para saber quem vai atacar e quem vai defender (se jogador 1 ou 2)
    Metodo aleatorio para saber qual dos adamons vai lutar ou defender
    Vai ser calculada a diferença de pontos entre ataque e defesa (diferencaVida):
    Se maior do que 0 (cenário atacante ganha):
    atacante ganha 20 pontos em vida e mais 10 no ataque
    defensor perde 20 pontos em vida
    ****Se a vida do defensor chegar a 0, ele morreu e é retirado da lista do jogador
    Se menor do que 0 (cenário em que o defensor ganha)
    defensor ganha 20 pontos em vida e mais 15 na defesa
    atacante perde 20 pontos em vida
   **** Se a vida do atacante chegar a 0, ele morreu e é retirado da lista do jogador
    O jogador que ganhar a partida ganha 25 pontos de saldo na carteira pra compra de Adamons
    *Adamons-frutas e Adamons-legumes
    */

    public void batalhaAutomatizadaComDoisJogadores(Jogador jogador1, Jogador jogador2) {
        List<Adamon> equipeAdamonJogadorUm = jogador1.getAdamons();
        List<Adamon> equipeAdamonJogadorDois = jogador2.getAdamons();
        Jogador atacante = new Jogador();
        Jogador defensor = new Jogador();
        if (Math.random() < 0.5) {
            atacante = jogador1;
            defensor = jogador2;
        } else {
            atacante = jogador2;
            defensor = jogador1;
        }
        while ((equipeAdamonJogadorUm.size() >0) && (equipeAdamonJogadorDois.size() > 0)) {
            Adamon adamonAtacante = atacante.getAdamons().get((int) (Math.random() * atacante.getAdamons().size()));
            Adamon adamonDefensor = defensor.getAdamons().get((int) (Math.random() * defensor.getAdamons().size()));
            int diferencaVida = adamonAtacante.getAtaque() - adamonDefensor.getDefesa();
            if (diferencaVida > 0) {
                cenarioAtacanteGanha(atacante, adamonAtacante);
                descrescimoVidaPerdedor(adamonDefensor);
                if (adamonDefensor.getVida() <= 0) {
                    List<Adamon> listaAtualAdamonsDefensor = new ArrayList<>(defensor.getAdamons());
                   listaAtualAdamonsDefensor.remove(adamonDefensor);
                   defensor.setAdamons(listaAtualAdamonsDefensor);
                    System.out.println("O adamon "+ adamonDefensor.getNome()+ "morreu");
                }
            } else {
                cenarioDefensorGanha(defensor, adamonDefensor);
                descrescimoVidaPerdedor(adamonAtacante);
                if (adamonAtacante.getVida() <= 0) {
                    List<Adamon> listaAtualAdamonsAtacante = new ArrayList<>(atacante.getAdamons());
                   listaAtualAdamonsAtacante.remove(adamonAtacante);
                   atacante.setAdamons(listaAtualAdamonsAtacante);
                }

            }
        }
    }

    public void cenarioAtacanteGanha(Jogador jogador, Adamon adamon) {
        acrescimoVidaAdamonGanhador(adamon);
        acrescimoAtaque(adamon);
        adicionarPontosCarteira(jogador);
        impressaoGanhadorNaTela(jogador, adamon);
    }

    public void cenarioDefensorGanha(Jogador jogador, Adamon adamon) {
        acrescimoVidaAdamonGanhador(adamon);
        acrescimoDefesa(adamon);
        adicionarPontosCarteira(jogador);
        impressaoGanhadorNaTela(jogador, adamon);
    }

    public void impressaoGanhadorNaTela(Jogador jogador, Adamon adamon) {
        System.out.println(jogador.getNickname() + " com o Adamon " + adamon.getNome() + " ganhou a partida");

    }

    public void adicionarPontosCarteira(Jogador jogador) {
        jogador.setSaldo(jogador.getSaldo().add(BigDecimal.valueOf(25)));
    }

    public void acrescimoVidaAdamonGanhador(Adamon adamon) {
        adamon.setVida(adamon.getVida() + 20);
    }

    public void acrescimoAtaque(Adamon adamon) {
        adamon.setAtaque(adamon.getAtaque() + 10);
    }

    public void acrescimoDefesa(Adamon adamon) {
        adamon.setDefesa(adamon.getDefesa() + 15);
    }

    public void descrescimoVidaPerdedor(Adamon adamon) {
        adamon.setVida(adamon.getVida() - 20);
    }


    public void comprarAdamon(Jogador jogador, Adamon adamon) {
        List<Adamon> equipeAdamonJogador = jogador.getAdamons();
        BigDecimal saldoAtual = jogador.getSaldo();
        BigDecimal precoAdamon = adamon.getPreco();

        boolean possuiSaldoSuficiente = saldoAtual.compareTo(precoAdamon) > 0;
        boolean possuiEspacoNaEquipe = equipeAdamonJogador.size() < 6;

        if (possuiEspacoNaEquipe && possuiSaldoSuficiente) {
            equipeAdamonJogador.add(adamon);
            jogador.setSaldo(saldoAtual.subtract(precoAdamon));
            atualizarJogador(jogador, jogador.getId());
        } else if (!possuiSaldoSuficiente) {
            throw new RuntimeException("Não possui saldo suficiente");
        } else if (!possuiEspacoNaEquipe) {
            throw new RuntimeException("Não possui espaço na equipe");
        }
    }

    public void atualizarJogador(Jogador jogador, Long idJogador) {
        encontrarJogadorPorId(idJogador);
        jogador.setId(idJogador);
        jogadorRepository.save(jogador);
    }

    public Jogador encontrarJogadorPorId(Long idJogador) {
        Optional<Jogador> optionalJogador = jogadorRepository.findById(idJogador);
        return optionalJogador
                .orElseThrow(() -> new RuntimeException("Não encontrado jogador com ID: " + idJogador));
    }

    public Jogador salvarJogador(SalvarJogadorDTO dto) {
        return jogadorRepository.save(JogadorDtoConverter.converterDto(dto));
    }


}
