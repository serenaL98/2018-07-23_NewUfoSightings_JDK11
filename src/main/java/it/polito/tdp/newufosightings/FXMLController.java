package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.appendText("Crea grafo...");
    	
    	int anno = Integer.parseInt(this.txtAnno.getText());
    	String forma = this.cmbBoxForma.getValue();
    	
    	if(forma == null) {
    		txtResult.setText("Scegliere una forma dal menù.\n");
    		return;
    	}
    	this.model.creaGrafo(forma, anno);
    	
    	txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI:  "+this.model.numeroArchi()+"\n\n");
    	
    	for(State s: this.model.elencoStati()) {
    		txtResult.appendText(""+this.model.pesiAdiacenti(s)+"\n");
    	}
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String annoinput = this.txtAnno.getText();
    	
    	try {
    		int anno = Integer.parseInt(annoinput);
    		
    		if(this.model.prendiForme(anno).size() == 0) {
    			txtResult.setText("Impossibile selezionare i dati per l'anno inserito.\n");
    			return;
    		}
    		this.cmbBoxForma.getItems().addAll(this.model.prendiForme(anno));
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico dal 1910 al 2014.");
    		return;
    	}

    }

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
