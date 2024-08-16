import java.util.Scanner;
import static java.lang.System.*;

class Aplicacao {

    static Scanner sc = new Scanner(in);

    public static void main(String a[]){
        
        programaEmExecucao:
            while(true){
                MenuPrincipal.exibirMenu();
                String input = sc.nextLine();
                if(MenuPrincipal.isTeclaSair(input)){
                    MenuPrincipal.perguntarSeConfirmaSaida();
                    String confirma = sc.nextLine();
                    if(MenuPrincipal.isTeclaConfirmaSair(confirma)){
                        break programaEmExecucao;
                    }
                }
                AgendaContato.Operacoes operacaoEscolhida = MenuPrincipal.obterOperacaoEscolhida(input);
                processar(operacaoEscolhida);
            }
    }

    static void processar(AgendaContato.Operacoes operacao){
        switch(operacao){
            case AgendaContato.Operacoes.ADICIONAR -> {
                TelaAdicionarContato.processar(sc);
            }
            case AgendaContato.Operacoes.LISTAR -> {
                AgendaContato.listar();
            }
            case AgendaContato.Operacoes.REMOVER -> {
                TelaRemoverContato.processar(sc);
            }
            case AgendaContato.Operacoes.DETALHAR -> {
                TelaDetalharContato.processar(sc);
            }
            case AgendaContato.Operacoes.EDITAR -> {
                TelaEditarContato.processar(sc);
            }
        };
    }
}

class MenuPrincipal {

    static String TECLA_PARA_SAIR = "S";
    static String TECLA_PARA_CONFIRMA_SAIR = "S";

    static void exibirMenu(){
        out.println("Escolha uma opção:");
        for(AgendaContato.Operacoes opcao : AgendaContato.Operacoes.values()){
            out.printf("%s - %s\n", opcao.ordinal()+1, opcao);
        }
        out.printf("%s - %s\n", TECLA_PARA_SAIR, "SAIR");
    }

    static void perguntarSeConfirmaSaida(){
        out.printf("Deseja mesmo sair? Confirme digitando %s\n", TECLA_PARA_CONFIRMA_SAIR);
    }

     static boolean isTeclaSair(String input){
        return TECLA_PARA_SAIR.equals(input);
    }

     static boolean isTeclaConfirmaSair(String input){
        return TECLA_PARA_CONFIRMA_SAIR.equals(input);
    }

    static AgendaContato.Operacoes obterOperacaoEscolhida(String input){
        int indiceDaOpcao = Integer.parseInt(input) - 1;
        AgendaContato.Operacoes opcaoEscolhida = AgendaContato.Operacoes.values()[indiceDaOpcao];
        out.printf("Processando o comando escolhido: %s\n", opcaoEscolhida);
        return opcaoEscolhida;
    }
}


class MenuEdicao{

    static String TECLA_PARA_SALVAR = "S";

    static void exibirMenu(){
        out.println("Escolha uma opção:");
        for(AgendaContato.Campos opcao : AgendaContato.Campos.values()){
            out.printf("%s - %s\n", opcao.ordinal()+1, opcao);
        }
        out.printf("%s - %s\n", TECLA_PARA_SALVAR, "SALVAR");
    }

    static boolean isTeclaSalvar(String input){
        return TECLA_PARA_SALVAR.equals(input);
    }

    static AgendaContato.Campos obterCampoASerSalvo(String input){
        int indiceDaOpcao = Integer.parseInt(input) - 1;
        AgendaContato.Campos opcaoEscolhida = AgendaContato.Campos.values()[indiceDaOpcao];
        return opcaoEscolhida;
    }
}

class TelaAdicionarContato{
    static void processar(Scanner sc){
        String valores[] = new String[AgendaContato.Campos.values().length];

        for(AgendaContato.Campos campo : AgendaContato.Campos.values()){
            out.printf("Digite o valor para o campo %s\n", campo);
            valores[campo.ordinal()] = sc.nextLine();
        }
        AgendaContato.adicionar(valores);
    }
}

class TelaRemoverContato{
    static void processar(Scanner sc){
        out.println("Qual o ID do contato a ser removido?");
        int id = Integer.parseInt(sc.nextLine());
        AgendaContato.remover(id);
    }
}

class TelaDetalharContato{
    static void processar(Scanner sc){
        out.println("Qual o ID do contato a ser visualizado?");
        int id = Integer.parseInt(sc.nextLine());
        AgendaContato.detalhar(id);
    }
}

class TelaEditarContato{
    static void processar(Scanner sc){
        out.println("Qual o ID do contato a ser editado?");
        int id = Integer.parseInt(sc.nextLine());
        AgendaContato.Campos campos[] = new AgendaContato.Campos[AgendaContato.Campos.values().length];
        String valores[] = new String[AgendaContato.Campos.values().length];
        while(true){
            out.println("Qual o campo deseja editar?");
            MenuEdicao.exibirMenu();
            String input = sc.nextLine();
            if(MenuEdicao.isTeclaSalvar(input)){
                break;
            }
            AgendaContato.Campos campoASerSalvo = MenuEdicao.obterCampoASerSalvo(input);
            out.printf("Digite o novo valor para o campo %s\n", campoASerSalvo);
            String novoValor = sc.nextLine();
            valores[campoASerSalvo.ordinal()]=novoValor;
        }
        AgendaContato.editar(id, valores);
    }
}

class AgendaContato {

    static String[][] contatos = new String[Configuracoes.LIMITE_CONTATOS_AGENDA][];

    static void adicionar(String[] valores){
        int indice = obterIndiceNovoContato();
        contatos[indice] = new String[Campos.values().length];
        for(int i=0; i<valores.length; i++){
            if(valores[i]!=null){
                contatos[indice][i] = valores[i];
            }
        }
        out.println("Contato adicionado com sucesso!");
    }

    static void remover(int indice){
        contatos[indice] = null;
        out.println("Contato removido com sucesso!");
    }

    static void editar(int id, String[] valores){
        for(int i=0; i<valores.length; i++){
            if(valores[i]!=null){
                contatos[id][i] = valores[i];
            }
        }
        out.println("Contato alterado com sucesso!");
    }
    
    static void listar(){
        for(int i = 0; i < contatos.length; i++){
            if(contatos[i]==null)
                continue;
            out.printf("%s, %s\n", i, contatos[i][0]);
        }
    }

    static void detalhar(int id){
        out.printf("%s, ", id);
        for(Campos campo : Campos.values()){
            out.printf("%s, ", contatos[id][campo.ordinal()]);
        }
        out.println();
    }

    private static int obterIndiceNovoContato(){
        int indice = -1;
        for(int i = 0; i < contatos.length; i++){
            if(contatos[i]==null){
                indice = i;
                break;
            }
        }
        return indice;
    }

    enum Campos {
        NOME, TELEFONE, EMAIL;
    }

    enum Operacoes {
        ADICIONAR, DETALHAR, EDITAR, REMOVER, LISTAR;
    }
}

class Configuracoes {

    static int LIMITE_CONTATOS_AGENDA = 100;

}