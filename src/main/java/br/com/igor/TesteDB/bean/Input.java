package br.com.igor.TesteDB.bean;

public class Input {
	private String agencia;
	private String conta;
	private float saldo;
	private String status;

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public Input(String agencia, String conta, float saldo, String status) {
		super();
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
	}

	public Input() {
	}

	@Override
	public String toString() {
		return "Input [agencia=" + agencia + ", conta=" + conta + ", saldo=" + saldo + ", status=" + status + "]";
	}

	

}
