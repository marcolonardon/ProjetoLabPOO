package br.unaerp;

public class Conta {
    private int id;
    private float saldo;
    private Usuario usuario;

    public Conta(int id, Usuario usuario) {
        this.id = id;
        this.saldo = 0;
        this.usuario = usuario; 
    }

    public float getSaldo() {
        return this.saldo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void depositar(float valor) {
        this.saldo += valor;
        usuario.registrarTransacao(valor, "Receita", "Depósito na conta");
    }

    public void sacar(float valor) {
        if (this.saldo < valor) {
            System.out.println("Saldo insuficiente.");
            return;
        }
        this.saldo -= valor;
        usuario.registrarTransacao(valor, "Despesa", "Saque da conta");
    }

    public void transferir(Conta contaPara, float valor) {
        if (this.saldo < valor) {
            System.out.println("Saldo insuficiente.");
            return;
        }

        this.saldo -= valor;
        contaPara.depositar(valor);
        usuario.registrarTransacao(valor, "Despesa", "Transferência para outra conta");
        contaPara.getUsuario().registrarTransacao(valor, "Receita", "Transferência recebida");
    }

    @Override
    public String toString() {
        return "Conta ID: " + id + ", Saldo: " + saldo + ", Usuário: " + usuario.getNome();
    }
}
