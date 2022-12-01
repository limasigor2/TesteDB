package br.com.igor.TesteDB.bean;

public class Output {
	private String agencia;
	private String conta;
	private float saldo;
	private String status;
	private Boolean result;

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

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Output(String agencia, String conta, float saldo, String status, Boolean result) {
		super();
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
		this.result = result;
	}

	public Output() {
	}

	@Override
	public String toString() {
		return "Output [agencia=" + agencia + ", conta=" + conta + ", saldo=" + saldo + ", status=" + status
				+ ", result=" + result + "]";
	}

}
