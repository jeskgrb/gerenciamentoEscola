package gerenciamento;

import com.entity.Professor;
import com.persist.EntityPersist;
import com.util.CriteriaGroup;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import messages.Gmessages;

@ViewScoped
@ManagedBean
public class GerenciarProfessor {
    private Professor professor, selecionado;
    private EntityPersist ep;
    private String busca, param;
    private List<Professor> professores;
    private boolean isAtivo;
    private Gmessages msg = new Gmessages();

    public GerenciarProfessor() {
        System.out.println("Ativado");
        selecionado = new Professor();
        professor = new Professor();
        ep = new EntityPersist();
        professores = ep.search(Professor.class, new CriteriaGroup("eq", "estado", "ativo", professor));
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }

    public Professor getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Professor selecionado) {
        this.selecionado = selecionado;
    }

    public boolean isIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public List<Professor> listar() {
        return ep.search(Professor.class);
    }
    
    public void cadastrarProfessor(ActionEvent ae) {
        try {
            ep.save(professor);
        } catch (Exception ex) {
            Logger.getLogger(GerenciarProfessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarProfessor(ActionEvent ae) {
        if(busca.trim().equals(""))
            professores = ep.search(Professor.class, new CriteriaGroup("eq", "estado", "ativo", null));
        else if(!param.equals("estado")) {
            professores = ep.search(Professor.class, new CriteriaGroup("eq", param, busca, null),
                    new CriteriaGroup("eq", "estado", "ativo", null));
        } else
            professores = ep.search(Professor.class, new CriteriaGroup("eq", param, busca, null));
    }

    public void selectProfessor(ActionEvent ae) {
        selecionado = (Professor) ae.getComponent().getAttributes().get("professor");
        setIsAtivo(!"inativo".equals(selecionado.getEstado()));
    }

    public void alterarProfessor(ActionEvent ae) {
        System.out.println("Alterando");
        try {
            ep.update(selecionado);
            msg.alterar(ae);
        } catch (Exception ex) {
            Logger.getLogger(GerenciarProfessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void removerProfessor(ActionEvent ae) {
        System.out.println("Removendo");
        selecionado.setEstadoInativo();
        try {
            ep.update(selecionado);
            msg.remover(ae);
            busca = "";
            param = "Nome";
            consultarProfessor(ae);
        } catch (Exception ex) {
            Logger.getLogger(GerenciarProfessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ativar(ActionEvent ae) {
        selecionado.setEstadoAtivo();
        msg.ativado(ae);
        setIsAtivo(false);
    }
}
