package br.com.igor.TesteDB.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import br.com.igor.TesteDB.bean.Input;
import br.com.igor.TesteDB.bean.Output;
import br.com.igor.TesteDB.service.ReceitaService;

public class FileProcessor implements ItemProcessor<Input, Output> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

	@Override
    public Output process(final Input input) throws Exception {
        
        LOGGER.info("Converting ( {} ) into ( )", input);
        ReceitaService receitaService = new ReceitaService();
        return new Output(
        		input.getAgencia(),
        		input.getConta(), 
        		input.getSaldo(), 
        		input.getStatus(), 
        		receitaService.atualizarConta(input.getAgencia(), input.getConta(), input.getSaldo(), input.getStatus()));
    }
}
