class Trabalhador {
    private String Nome;
    private double salarioBruto;
    private double descontoINSS;
    private int numeroDependentes;
    private double totalDescontosIRRF;
    private String cpf;
    private String cep;
    private String endereco;

    public String getNome() {
        return Nome;
    }
    public double getSalarioBruto() {
        return salarioBruto;
    }
    public double getDescontoINSS() {
        return descontoINSS;
    }
    public int getNumeroDependentes() {
        return numeroDependentes;
    }
    public double getTotalDescontosIRRF() {
        return totalDescontosIRRF;
    }
    public String getCpf() {
        return cpf;
    }
    public String getCep() {
        return cep;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public void setDescontoINSS(double descontoINSS) {
        this.descontoINSS = descontoINSS;
    }

    public void setNumeroDependentes(int numeroDependentes) {
        this.numeroDependentes = numeroDependentes;
    }

    public void setTotalDescontosIRRF(double totalDescontosIRRF) {
        this.totalDescontosIRRF = totalDescontosIRRF;
    }

    public void setCpf(String cpf2) {
        this.cpf = cpf2;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double calcularIRRF() {
        double baseCalculo = salarioBruto - descontoINSS - totalDescontosIRRF;
        double irrf = 0.0;

        if (baseCalculo <= 1903.98) {
            irrf = 0.0;
        } else if (baseCalculo <= 2826.65) {
            irrf = baseCalculo * 0.075 - 142.80;
        } else if (baseCalculo <= 3751.05) {
            irrf = baseCalculo * 0.15 - 354.80;
        } else if (baseCalculo <= 4664.68) {
            irrf = baseCalculo * 0.225 - 636.13;
        } else {
            irrf = baseCalculo * 0.275 - 869.36;
        }
        return irrf;
    }

    public double calcularSalarioLiquido() {
        return salarioBruto - descontoINSS - calcularIRRF();
    }
}