package aplicacao;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

import java.util.List;

import javax.swing.JOptionPane;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.query.Query;

import modelo.Aluno;
import modelo.Pessoa;
import modelo.Professor;
import modelo.Telefone;


public class Atualizar {
	protected static ObjectContainer manager;

	public Atualizar(){
		//abrirBancoLocal();
		abrirBancoServidor();
		alterar();
		manager.close();	
		System.out.println("\n\n aviso: feche sempre o plugin eclipse antes de executar aplica��o");
	}

	public void abrirBancoLocal(){
		EmbeddedConfiguration config =  Db4oEmbedded.newConfiguration(); 
		config.common().messageLevel(3);  // 0,1,2,3...
		
		config.common().objectClass(Pessoa.class).cascadeOnDelete(true);;
		config.common().objectClass(Pessoa.class).cascadeOnUpdate(true);;
		config.common().objectClass(Pessoa.class).cascadeOnActivate(true);
		config.common().objectClass(Aluno.class).cascadeOnDelete(true);;
		config.common().objectClass(Aluno.class).cascadeOnUpdate(true);;
		config.common().objectClass(Aluno.class).cascadeOnActivate(true);
		config.common().objectClass(Professor.class).cascadeOnDelete(true);;
		config.common().objectClass(Professor.class).cascadeOnUpdate(true);;
		config.common().objectClass(Professor.class).cascadeOnActivate(true);
		config.common().objectClass(Telefone.class).cascadeOnDelete(true);;
		config.common().objectClass(Telefone.class).cascadeOnUpdate(true);;
		config.common().objectClass(Telefone.class).cascadeOnActivate(true);		
		manager = 	Db4oEmbedded.openFile(config, "banco.db4o");
		IDControl.registrarManager(manager); 
	}
	public static void abrirBancoServidor(){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration( ) ;
		config.common().messageLevel(0);   //0,1,2,3,4
		config.common().objectClass(Pessoa.class).cascadeOnDelete(true);;
		config.common().objectClass(Pessoa.class).cascadeOnUpdate(true);;
		config.common().objectClass(Pessoa.class).cascadeOnActivate(true);
		config.common().objectClass(Aluno.class).cascadeOnDelete(true);;
		config.common().objectClass(Aluno.class).cascadeOnUpdate(true);;
		config.common().objectClass(Aluno.class).cascadeOnActivate(true);
		config.common().objectClass(Professor.class).cascadeOnDelete(true);;
		config.common().objectClass(Professor.class).cascadeOnUpdate(true);;
		config.common().objectClass(Professor.class).cascadeOnActivate(true);
		config.common().objectClass(Telefone.class).cascadeOnDelete(true);;
		config.common().objectClass(Telefone.class).cascadeOnUpdate(true);;
		config.common().objectClass(Telefone.class).cascadeOnActivate(true);		
		String ip = JOptionPane.showInputDialog("Digite o IP do servidor");
		if (ip==null || ip.isEmpty())	{
			System.out.println("ip invalido");
			System.exit(0);
		}
		manager = Db4oClientServer.openClient(config,ip,34000,"usuario1","senha1");	
		IDControl.registrarManager(manager); 
	}
	
	public void alterar(){
		System.out.println("alterando telefones de joao...");
		Pessoa p = localizarPessoa("joao");	
		if(p != null) {
			p.setNome("joana");
			
			//adicionar um novo telefone
			p.adicionarTelefone(new Telefone("99999999"));
			
			//remover um telefone ja existente
			Telefone t = p.localizarTelefone("88881111");
			p.removerTelefone(t);  
			t.setPessoa(null);	 	//este telefone fica orfao

			manager.store(p);
			manager.store(t);	
		//	manager.delete(t);		//apagar o telefone orfao
			manager.commit();
		}else
			System.out.println("inexistente");
	}

	public Pessoa localizarPessoa(String nome){
		Query q = manager.query();
		q.constrain(Pessoa.class);  		// select p from Pessoa p where nome=:nome		
		q.descend("nome").constrain(nome);		 
		List<Pessoa> resultados = q.execute();

		if(resultados.size()>0) {
			Pessoa p = resultados.get(0);
			return p;
		}else
			return null;
	} 


	//=================================================
	public static void main(String[] args) {
		new Atualizar();
	}
}
