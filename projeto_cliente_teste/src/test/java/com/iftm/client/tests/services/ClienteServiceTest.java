package com.iftm.client.tests.services;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ClienteServiceTest {
		
	@Autowired
	private ClientService servico;

	@Test
	public void testarSeDeleteNaoRetornaNadaQuandoIdExiste() {
		Long idExistente = 1L;
		Assertions.assertDoesNotThrow(()->servico.delete(idExistente));
	}

	@Test
	public void testarSeDeleteLancaExceptionQuandoIdNaoExiste() {
		Long idNaoExistente = 5000L;
		Assertions.assertThrows(ResourceNotFoundException.class, ()->servico.delete(idNaoExistente));
	}

	@Test
	public void testarRetornoDaPaginaComTodosOsClientes() {
		PageRequest pageRequest = PageRequest.of(11,1);
		List<Client> lista = new ArrayList<Client>();
		lista.add(new Client(12L, "'Jorge Amado", "10204374161", 2500.0, Instant.parse("1975-11-10T07:00:00Z"), 0));

		Page<Client> pagina = new PageImpl<>(lista, pageRequest, lista.size());
		Page<ClientDTO> resultado = servico.findAllPaged(pageRequest);

		Assertions.assertFalse(resultado.isEmpty());
		Assertions.assertEquals(lista.size(), resultado.getNumberOfElements());
		for(int i = 0; i < lista.size(); i++) {
			Assertions.assertEquals(lista.get(i), resultado.toList().get(i).toEntity());
		}
	}

	@Test
	public void testarRetornoDoMetodoFindByIncome() {
		PageRequest pageRequest = PageRequest.of(0, 1, Direction.valueOf("ASC"), "name");
		Double  entrada = 4500.00;
		List<Client> lista = new ArrayList<Client>();
		lista.add(new Client(6L, "Djamila Ribeiro", "10619244884", 4500.0, Instant.parse("1975-11-10T07:00:00Z"), 1));

		Page<Client> pagina = new PageImpl<>(lista, pageRequest, lista.size());
		Page<ClientDTO> resultado = servico.findByIncome(pageRequest, entrada);

		Assertions.assertFalse(resultado.isEmpty());
		Assertions.assertEquals(lista.size(), resultado.getNumberOfElements());
		for (int i = 0; i < lista.size(); i++) {
			Assertions.assertEquals(lista.get(i), resultado.toList().get(i).toEntity());
		}
	}

	@Test
	public void testarSeFindByIdRetornaClientDTOQuandoExistir() {
		PageRequest pageRequest = PageRequest.of(0, 1);
		Long idExistente = 8l;
		Optional<Client> client = Optional.of(new Client(8L, "Djamila Ribeiro", "10619244884", 4500.0, Instant.parse("1975-11-10T07:00:00Z"), 1));
		ClientDTO resultado = servico.findById(idExistente);

		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(client.get(), resultado.toEntity());
	}

	@Test
	public void testarSeFindByIdLancaExceptionQuandoClienteNaoExistir() {
		Long idInexistente = 50000L;
		Assertions.assertThrows(ResourceNotFoundException.class, () -> servico.findById(idInexistente));
	}

	@Test
	public void testarRetornoUpdateQuandoClienteDTOExistir() {
		Long idExistente = 1L;
		Client cliente = new Client(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2);
		ClientDTO clienteModificado = new ClientDTO(1L, "Conceição Evaristo", "10619244881", 10000.0, Instant.parse("2020-07-13T20:50:00Z"), 2);
		ClientDTO resultado = servico.update(idExistente, clienteModificado);

		Assertions.assertEquals(cliente, resultado.toEntity());
	}

	@Test
	public void testarSeUpdateLancaExceptionQuandoClienteDTONaoExistir() {
		Long idInexistente = 50000L;
		Client cliente = new Client(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2);
		ClientDTO clienteModificado = new ClientDTO(1L, "Conceição Evaristo", "10619244881", 10000.0, Instant.parse("2020-07-13T20:50:00Z"), 2);

		Assertions.assertThrows(ResourceNotFoundException.class, ()-> servico.update(idInexistente, clienteModificado));
	}

	@Test
	public void testarSeInsertRetornaClientDTOAoInserirNovoCliente() {
		Client cliente =  new Client(13L, "Maria", "12345698700", 20000.0, Instant.parse("2010-08-14T20:50:00Z"), 4);
		ClientDTO resultado = servico.insert(new ClientDTO(cliente));

		Assertions.assertEquals(cliente, resultado.toEntity());
	}

}
