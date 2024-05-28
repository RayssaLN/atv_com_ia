import java.io.*;
import java.net.*;
import java.util.*;

public class App {
    private static final String ARQUIVO_DADOS = "trabalhadores.txt";
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Digite o nome do trabalhador:");
            String nome = scanner.nextLine().trim();

            System.out.println("Digite o salário bruto:");
            double salarioBruto = lerDouble(scanner);

            System.out.println("Digite o desconto do INSS:");
            double descontoINSS = lerDouble(scanner);

            System.out.println("Digite o número de dependentes:");
            int numeroDependentes = lerInt(scanner);

            System.out.println("Digite o valor total de descontos para dedução do IRRF:");
            double totalDescontosIRRF = lerDouble(scanner);

            System.out.println("Digite o CPF:");
            String cpf = scanner.nextLine().trim();
            if (!validarCPF(cpf)) {
                System.out.println("CPF inválido.");
                return;
            }

            System.out.println("Digite o CEP:");
            String cep = scanner.nextLine().trim();
            String endereco = buscarEnderecoPorCEP(cep);

            Trabalhador trabalhador = new Trabalhador();
            trabalhador.setNome(nome);
            trabalhador.setSalarioBruto(salarioBruto);
            trabalhador.setDescontoINSS(descontoINSS);
            trabalhador.setNumeroDependentes(numeroDependentes);
            trabalhador.setTotalDescontosIRRF(totalDescontosIRRF);
            trabalhador.setCpf(cpf);
            trabalhador.setCep(cep);
            trabalhador.setEndereco(endereco);

            double salarioLiquido = trabalhador.calcularSalarioLiquido();
            System.out.println("Salário líquido: " + salarioLiquido);
            salvarDadosTrabalhador(trabalhador);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira os dados corretamente.");
        } catch (IOException e) {
            System.out.println("Erro ao acessar arquivo ou rede: " + e.getMessage());
        }
    }
    private static double lerDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número válido:");
            }
        }
    }

    private static int lerInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro válido:");
            }
        }
    }

    private static boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    private static String buscarEnderecoPorCEP(String cep) throws IOException {
        String apiURL = "http://viacep.com.br/ws/" + cep + "/json/";
        URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            if (jsonObject.has("erro")) {
                return "CEP não encontrado.";
            }
            String logradouro = jsonObject.get("logradouro").toString();
            String bairro = jsonObject.get("bairro").toString();
            String localidade = jsonObject.get("localidade").toString();
            String uf = jsonObject.get("uf").toString();
            return logradouro + ", " + bairro + ", " + localidade + " - " + uf;
        }
    }

    private static void salvarDadosTrabalhador(Trabalhador trabalhador) throws IOException {
        List<Trabalhador> trabalhadores = carregarDadosTrabalhadores();
        boolean exists = false;

        for (Trabalhador t : trabalhadores) {
            if (t.getCpf().equals(trabalhador.getCpf())) {
                t.setNome(trabalhador.getNome());
                t.setSalarioBruto(trabalhador.getSalarioBruto());
                t.setDescontoINSS(trabalhador.getDescontoINSS());
                t.setNumeroDependentes(trabalhador.getNumeroDependentes());
                t.setTotalDescontosIRRF(trabalhador.getTotalDescontosIRRF());
                t.setCep(trabalhador.getCep());
                t.setEndereco(trabalhador.getEndereco());
                exists = true;
                break;
            }
        }

        if (!exists) {
            trabalhadores.add(trabalhador);
        }

        salvarTodosTrabalhadores(trabalhadores);
    }

    private static List<Trabalhador> carregarDadosTrabalhadores() throws IOException {
        List<Trabalhador> trabalhadores = new ArrayList<>();
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return trabalhadores;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                Trabalhador trabalhador = new Trabalhador();
                trabalhador.setNome(campos[0]);
                trabalhador.setSalarioBruto(Double.parseDouble(campos[1]));
                trabalhador.setDescontoINSS(Double.parseDouble(campos[2]));
                trabalhador.setNumeroDependentes(Integer.parseInt(campos[3]));
                trabalhador.setTotalDescontosIRRF(Double.parseDouble(campos[4]));
                trabalhador.setCpf(campos[5]);
                trabalhador.setCep(campos[6]);
                trabalhador.setEndereco(campos[7]);
                trabalhadores.add(trabalhador);
            }
        }

        return trabalhadores;
    }

    private static void salvarTodosTrabalhadores(List<Trabalhador> trabalhadores) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_DADOS))) {
            for (Trabalhador trabalhador : trabalhadores) {
                writer.write(trabalhador.getNome() + ";" +
                             trabalhador.getSalarioBruto() + ";" +
                             trabalhador.getDescontoINSS() + ";" +
                             trabalhador.getNumeroDependentes() + ";" +
                             trabalhador.getTotalDescontosIRRF() + ";" +
                             trabalhador.getCpf() + ";" +
                             trabalhador.getCep() + ";" +
                             trabalhador.getEndereco() + "\n");
            }
        }
    }
}