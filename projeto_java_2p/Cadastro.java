package projeto_java_2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Cadastro {

    // Método para realizar o cadastro de vacas
    public static void cadastro() {
        // Solicita as informações ao usuário
        String input = JOptionPane.showInputDialog("Informe a vaca para cadastro: \n- Código | Nome | Raça | Variação");

        if (input != null) {
            // Divide as informações inseridas pelo usuário
            String[] div = input.split(", ");

            // Verifica se as informações foram inseridas corretamente
            if (div.length == 4) {
                try {
                    int codigo = Integer.parseInt(div[0].trim());

                    // Verifica se o código já existe
                    if (codigoExistente(codigo)) {
                        JOptionPane.showMessageDialog(null, "Erro: Código já existe. Escolha outro código.");
                    } else {
                        // Adiciona os dados da vaca nos vetores e salva o cadastro em arquivo
                        adicionarDadosVaca(div);
                        salvarCadastroEmArquivo(codigo, div);
                        JOptionPane.showMessageDialog(null, "Vaca cadastrada com sucesso.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Erro: Código não é um número válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "A entrada não foi dividida corretamente em 4 partes.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Entrada nula. Por favor, forneça uma entrada válida.");
        }
    }

    // Método que verifica se o código já existe no vetor de códigos
    public static boolean codigoExistente(int codigo) {
        for (int i = 0; i < VarShare.cod.length; i++) {
            if (VarShare.cod[i] == codigo) {
                return true;
            }
        }
        return false;
    }

    // Método para adicionar as informações nos respectivos vetores
    private static void adicionarDadosVaca(String[] div) {
        int i = (int) (VarShare.id - 1);

        try {
            VarShare.cod[i] = Integer.parseInt(div[0].trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Código não é um número válido.");
            return;
        }

        VarShare.nome[i] = div[1].trim();
        VarShare.raca[i] = div[2].trim();
        VarShare.variacao[i] = div[3].trim();
        VarShare.id++;
    }

    // Método para salvar os dados em arquivo .txt
    private static void salvarCadastroEmArquivo(int codigo, String[] div) {
        // Cria o diretório se não existir
        String diretorio = "cadastros_vacas";
        File diretorioFile = new File(diretorio);

        try {
            if (!diretorioFile.exists()) {
                diretorioFile.mkdirs();
            }

            // Cria o caminho completo do arquivo
            String caminhoArquivo = diretorio + "/vaca_" + codigo + ".txt";

            // Salva os respectivos dados no arquivo txt, especificando cada dado
            try (BufferedWriter w = new BufferedWriter(new FileWriter(caminhoArquivo))) {
                w.write("Código: " + div[0].trim());
                w.newLine();
                w.write("Nome: " + div[1].trim());
                w.newLine();
                w.write("Raça: " + div[2].trim());
                w.newLine();
                w.write("Variação: " + div[3].trim());
                w.newLine();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o cadastro em arquivo: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Erro de permissão ao criar o diretório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método responsável por imprimir os cadastros de cada vaca
    public static void listarCadastros() {
        // Verifica se o diretório de cadastros existe
        String diretorio = "cadastros_vacas";
        File diretorioFile = new File(diretorio);

        if (diretorioFile.exists() && diretorioFile.isDirectory()) {
            // Obtém a lista de arquivos no diretório
            File[] arquivos = diretorioFile.listFiles();

            if (arquivos != null && arquivos.length > 0) {
                StringBuilder cadastrosFormatados = new StringBuilder();

                // Adiciona o cabeçalho
                cadastrosFormatados.append(" Código | Nome | Raça | Variação\n");

                for (File arquivo : arquivos) {
                    if (arquivo.isFile()) {
                        // Lê as informações do arquivo e adiciona à string formatada
                        String infoCadastro = lerCadastro(arquivo);
                        cadastrosFormatados.append(infoCadastro).append("\n");
                    }
                }

                // Exibe os cadastros formatados usando JOptionPane
                JOptionPane.showMessageDialog(null, "Cadastros:\n\n" + cadastrosFormatados.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Não há cadastros disponíveis.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Diretório de cadastros não encontrado.");
        }
    }

    // Método responsável por ler os dados de cadastro
    private static String lerCadastro(File arquivo) {
        StringBuilder infoCadastro = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona cada linha do arquivo à string formatada
                infoCadastro.append(linha).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o cadastro do arquivo: " + arquivo.getName());
            e.printStackTrace();
        }

        return formatarCadastro(infoCadastro.toString());
    }

    // Método que formata a saída dos dados
    private static String formatarCadastro(String cadastro) {
        // Modificar conforme necessário para o formato específico do seu arquivo
        String[] linhas = cadastro.split("\n");
        StringBuilder cadastroFormatado = new StringBuilder();

        for (String linha : linhas) {
            String[] partes = linha.split(":");
            if (partes.length == 2) {
                cadastroFormatado.append(partes[1].trim()).append(" | ");
            }
        }

        // Remove o último " | " se existir
        if (cadastroFormatado.length() > 3) {
            cadastroFormatado.setLength(cadastroFormatado.length() - 3);
        }

        return cadastroFormatado.toString();
    }

    // Método para carregar os cadastros existentes
    public static void carregarCadastros() {
        String diretorio = "cadastros_vacas";
        File diretorioFile = new File(diretorio);

        if (diretorioFile.exists() && diretorioFile.isDirectory()) {
            File[] arquivos = diretorioFile.listFiles();

            if (arquivos != null && arquivos.length > 0) {
                for (File arquivo : arquivos) {
                    if (arquivo.isFile()) {
                        lerCadastroEAdicionar(arquivo);
                    }
                }
            }
        }
    }

    // Método para ler o cadastro de um arquivo e adicionar aos vetores
    private static void lerCadastroEAdicionar(File arquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;

            int i = (int) (VarShare.id - 1);

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    switch (partes[0].trim()) {
                        case "Código":
                            VarShare.cod[i] = Integer.parseInt(partes[1].trim());
                            break;
                        case "Nome":
                            VarShare.nome[i] = partes[1].trim();
                            break;
                        case "Raça":
                            VarShare.raca[i] = partes[1].trim();
                            break;
                        case "Variação":
                            VarShare.variacao[i] = partes[1].trim();
                            break;
                    }
                }
            }

            VarShare.id++;
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o cadastro do arquivo: " + arquivo.getName());
            e.printStackTrace();
        }
    }
}
