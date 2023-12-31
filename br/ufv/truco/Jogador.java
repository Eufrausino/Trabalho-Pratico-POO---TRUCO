package br.ufv.truco;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Jogador {
    private String nome;

    private ArrayList<Carta> cartas = new ArrayList<>();

    private boolean ehMaquina = false;

    private Scanner scan = Utils.scan;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public Jogador(String nome, boolean ehMaquina) {
        this.nome = nome;
        this.ehMaquina = ehMaquina;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Carta> getCartas() {
    	return cartas;
    }

    @Override
    public String toString() {
        return nome;
    }

    public void largaCartas() {
        cartas.clear();
    }

    public void recebeCarta(Carta carta) {
        cartas.add(carta);
    }

    // Pede ao usuário que escolha uma ação para que o jogador tome. As
    // opções incluem jogar carta, pedir truco (se possível), gritar ou
    // correr.
    public Resposta age(boolean naoPodeTrucar) {
        int resp;
        System.out.printf("== Jogador (%s) ==\n", nome);
        if(!naoPodeTrucar) {
            while(true) {
                System.out.println("Qual ação você deseja fazer?");
                System.out.print("(1) jogar carta, (2) pedir truco, (3) gritar, (4) correr => ");
                try {
                    resp = scan.nextInt();
                } catch(InputMismatchException ex) {
                    System.err.println("[!] Inteiro inválido! Tente novamente");
                    scan.nextLine(); // remove '\n' sobressalente
                    continue;
                }
                switch(resp) {
                    case 1:
                        return Resposta.ACEITA;
                    case 2:
                        return Resposta.AUMENTA;
                    case 3:
                        grita();
                        break;
                    case 4:
                        return Resposta.CORRE;
                    default:
                        System.err.println("[!] Resposta não reconhecida");
                }
            }
        } else {
            while(true) {
                System.out.println("Qual ação você deseja fazer?");
                System.out.print("(1) jogar carta, (2) gritar, (3) correr => ");
                try {
                    resp = scan.nextInt();
                } catch(InputMismatchException ex) {
                    System.err.println("[!] Inteiro inválido! Tente novamente");
                    scan.nextLine(); // remove '\n' sobressalente
                    continue;
                }
                switch(resp) {
                    case 1:
                        return Resposta.ACEITA;
                    case 2:
                        grita();
                        break;
                    case 3:
                        return Resposta.CORRE;
                    default:
                        System.err.println("[!] Resposta não reconhecida");
                }
            }
        }
    }

    // Pede ao usuário que escolha como responder a um desafio de truco
    public Resposta responde() {
        if(ehMaquina) return Resposta.ACEITA; // mudar isso
        while(true) {
            System.out.printf("== Responder (%s) ==\n", nome);
            System.out.println("Corre, aceita ou pede mais?");
            System.out.print("(1) correr, (2) aceitar, (3) pedir mais => ");
            int resp;
            try {
                resp = scan.nextInt();
            } catch(InputMismatchException ex) {
                System.err.println("[!] Inteiro inválido! Tente novamente");
				scan.nextLine(); // remove '\n' sobressalente
                continue;
            }
            switch(resp) {
                case 1:
                    System.out.println("Correu!");
                    return Resposta.CORRE;
                case 2:
                    System.out.println("Aceitou!");
                    return Resposta.ACEITA;
                case 3:
                    return Resposta.AUMENTA;
                default:
                    System.err.println("[!] Resposta inválida, tente novamente");
            }
            System.out.println();
        }
    }

    // Simplesmente, grita da maneira especificada pelo usuário
    public void grita() {
        String grito;
        if(ehMaquina) {
            grito = "AAAAAA";
        } else {
            System.out.print("Grito sinistro: ");
            scan.nextLine(); // consome um '\n' que com certeza já estará na entrada
            grito = scan.nextLine();
        }
        System.out.println(nome + " bate na mesa e grita: " + grito + "\n");
    }

    // Pede ao usuário que escolha uma carta para jogar
    public Carta jogaCarta(boolean podeEncoberta, boolean verCartas) {
        int i = 1, indiceEscolhida;
        System.out.printf("\n-- Jogar carta (%s) --\n", nome);
        StringBuilder s = new StringBuilder();
        for(Carta carta : cartas) {
            s.append(i + ". [" + (verCartas ? carta : "??") + "] ");
            ++i;
        }
        System.out.println(s.toString());
        while(true) {
            System.out.print("Carta a jogar => ");
            try {
                indiceEscolhida = scan.nextInt();
            } catch(InputMismatchException ex) {
                System.err.println("[!] Inteiro inválido! Tente novamente");
				scan.nextLine(); // remove '\n' sobressalente
                continue;
            }
            if(indiceEscolhida >= 1 && indiceEscolhida <= i) break;
            else System.err.println("[!] Carta inválida! Escolha novamente");
        }
        boolean continua = true;
        Carta c = cartas.remove(indiceEscolhida - 1);
        if(podeEncoberta) {
            while(continua) {
                System.out.print("(1) jogar normalmente, (2) jogar encoberta => ");
                int resp;
                try {
                    resp = scan.nextInt();
                } catch(InputMismatchException ex) {
                    System.err.println("[!] Inteiro inválido! Tente novamente");
                    scan.nextLine(); // remove '\n' sobressalente
                    continue;
                }
                switch(resp) {
                    case 1:
                        c.setEncoberta(false);
                        continua = false;
                        break;
                    case 2:
                        c.setEncoberta(true);
                        continua = false;
                        break;
                    default:
                        System.err.println("[!] Resposta não reconhecida");
                }
            }
        }
        System.out.println();
        return c;
    }
}
