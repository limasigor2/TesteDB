package br.com.igor.TesteDB.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.context.properties.bind.BindException;

import br.com.igor.TesteDB.bean.Input;

public class InputMapper implements FieldSetMapper<Input> {

	@Override
	public Input mapFieldSet(FieldSet fieldSet) throws BindException {
		Input person = new Input();
		person.setAgencia(fieldSet.readString("agencia"));
		person.setConta(fieldSet.readString("conta"));
		String saldo = fieldSet.readString("saldo").replace(',', '.');
		person.setSaldo(Float.valueOf(saldo));
		person.setStatus(fieldSet.readString("status"));
		return person;
	}
}