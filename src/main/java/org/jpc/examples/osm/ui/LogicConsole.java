package org.jpc.examples.osm.ui;

import static org.jpc.util.ThreadLocalLogicEngine.getLogicEngine;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.PrologEngineConfiguration;
import org.jpc.examples.osm.imp.MapQuery;
import org.jpc.examples.osm.imp.OsmDataLoader;
import org.jpc.term.Term;
import org.jpc.util.LogicEngineManager;
import org.jpc.util.LogicResourceLoader;
import org.jpc.util.ThreadLocalLogicEngine;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

public class LogicConsole extends GridPane {

	public static final Font TITLE_FONT = Font.font("Helvetica", FontWeight.NORMAL, 20);
	
	final Multimap<String, PrologEngineConfiguration> groupedEngineConfigurations;
	final ComboBox logicEnginesComboBox;
	final ComboBox bridgeLibraryComboBox;
	private OsmBrowser osmBrowser;
	private WebEngine webEngine;
	private PrologEngine logicEngine;
	
	public LogicConsole(WebEngine webEngine) {
		this.webEngine = webEngine;
		LogicEngineManager manager = LogicEngineManager.getDefault();
		manager.register(LogicEngineManager.findConfigurations());
		groupedEngineConfigurations = manager.groupByLogicEngine();
		final Multiset<String> logicEnginesMultiset = groupedEngineConfigurations.keys();
		Set<String> logicEngines = new HashSet(Arrays.asList(logicEnginesMultiset.toArray()));
		
		//setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));
		
		Text logicConsoleTitle = new Text("Logic Console settings");
		logicConsoleTitle.setFont(TITLE_FONT);
		add(logicConsoleTitle, 0, 0, 2, 1);
		
		
		logicEnginesComboBox = new ComboBox();
		bridgeLibraryComboBox = new ComboBox();
		
		logicEnginesComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.equals(oldValue)) {
					Set<String> bridgeLibraries = new HashSet<>();
					String selectedLogicEngine = (String) logicEnginesComboBox.getValue();
					Collection<PrologEngineConfiguration> logicEngineConfigurations = groupedEngineConfigurations.get(selectedLogicEngine);
					for(PrologEngineConfiguration logicEngineConfiguration : logicEngineConfigurations) {
						bridgeLibraries.add(logicEngineConfiguration.getLibraryName());
					}
					bridgeLibraryComboBox.getItems().clear();
					bridgeLibraryComboBox.getItems().addAll(bridgeLibraries);
					if(!bridgeLibraries.isEmpty()) {
						bridgeLibraryComboBox.setValue(bridgeLibraries.iterator().next());
					}	
				}
			}
		});
		
		logicEnginesComboBox.getItems().addAll(logicEngines);

		add(new Label("Prolog engine:"), 0,1);
		add(logicEnginesComboBox, 1,1);
		add(new Label("Bridge library:"), 0,2);
		add(bridgeLibraryComboBox, 1,2);
		
		
		
		final TextField fileTextField = new TextField();
		Button browseFileButton = new Button("...");
		browseFileButton.setOnAction(new EventHandler<ActionEvent>() {
			File file = null;
			@Override
			public void handle(ActionEvent event) {
				ExtensionFilter ef = new ExtensionFilter("OSM files (*.osm, *.xml)", "*.osm", "*.xml");
				//FileChooserBuilder fcb = FileChooserBuilder.create();
				//String currentDir = System.getProperty("user.dir") + File.separator;
				//FileChooser fc = fcb.title("Open Dialog").initialDirectory(new File(currentDir)).extensionFilters(ef).build();
				
				
				FileChooser fc = new FileChooser();
				fc.setTitle("Open Dialog");
				fc.getExtensionFilters().add(ef);
				if(file != null) {
					//System.out.println(file.exists());
	            	//System.out.println(file.isDirectory());
	            	
					File directoryPreviousFile = file.getParentFile();
	                fc.setInitialDirectory(directoryPreviousFile);
	            } else {
	            	String currentDir = System.getProperty("user.dir") + File.separator;
	            	//String currentDir = System.getProperty("user.home");
	            	file = new File(currentDir);
	            	//System.out.println(file.exists());
	            	//System.out.println(file.isDirectory());
	            	fc.setInitialDirectory(file);
	            }

				File selectedFile = fc.showOpenDialog(LogicConsole.this.getScene().getWindow());
				if(selectedFile != null) {
					file = selectedFile; //so next time the dialog will open in the same directory
					fileTextField.setText(selectedFile.getAbsolutePath());
				}
			}
			
		});

		Button loadFileButton = new Button("Load file");
		final ProgressIndicator progress = new ProgressIndicator();
		
		loadFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String osmFile = fileTextField.getText().trim();
				if(osmFile.isEmpty())
					SimpleDialog.error("Please select a file with OSM data to load").showDialog();
				else {
					if(logicEngine != null || initializeLogicEngine()) {
						progress.setVisible(true); //this is affecting the progress bar only after the handle method has returned
						//progress.setProgress(0);
						try {
							new OsmDataLoader(logicEngine).load(osmFile);
							progress.setProgress(1);
						} catch(RuntimeException e) {
							progress.setVisible(false);
							throw e; 
						}
						enableQueryOptions();
					}
				}
			}
		});
		
		add(new Label("OSM file:"), 0,3);
		add(fileTextField, 1,3);
		add(browseFileButton, 2,3);
		add(loadFileButton, 3,3);
		add(progress, 4,3);
		progress.setVisible(false);
		//progress.setDisable(true);
		if(!logicEngines.isEmpty()) {
			logicEnginesComboBox.setValue(logicEngines.iterator().next());
		} else {
			SimpleDialog.warning("Impossible to find available logic engines. The application will not work correctly.").showDialog();
		}
	}
	
	private boolean initializeLogicEngine() {
		if(verifyLogicEngineSelection()) {
			logicEngine = getSelectedLogicEngine();
			ThreadLocalLogicEngine.setLogicEngine(logicEngine);
			new LogicResourceLoader(getLogicEngine()).logtalkLoad(MapQuery.LOADER_FILE);
			disableEngineConfigurationOptions();
			return true;
		} else
			return false;
	}
	
	private PrologEngine getSelectedLogicEngine() {
		String logicEngineName = (String)logicEnginesComboBox.getValue();
		Collection<PrologEngineConfiguration> configurations = groupedEngineConfigurations.get(logicEngineName);
		String libraryName = (String) bridgeLibraryComboBox.getValue();
		for(PrologEngineConfiguration config : configurations) {
			if(config.getLibraryName().equals(libraryName))
				return config.getEngine();
		}
		return null;
	}
	
	/**
	 * Verify that a logic engine can be instantiated, otherwise shows an error message
	 * @return true if a logic engine can be instantiated, false otherwise
	 */
	private boolean verifyLogicEngineSelection() {
		if(((String)logicEnginesComboBox.getValue()).trim().isEmpty() || ((String)bridgeLibraryComboBox.getValue()).trim().isEmpty()) {
			SimpleDialog.error("Please select a logic engine and a bridge library.").showDialog();
			return false;
		} else
			return true;
	}
	
	public void disableEngineConfigurationOptions() {
		logicEnginesComboBox.setDisable(true);
		bridgeLibraryComboBox.setDisable(true);
	}
	
	public void disableQueryOptions() {
		webEngine.executeScript("g_disableQueryOptions()");
	}

	public void enableQueryOptions() {
		webEngine.executeScript("g_enableQueryOptions()");
	}
	
	public void query(String queryString) {
		Multimap<String, Term> mapQueryResult = evaluateQuery();
		drawQuery(mapQueryResult);
	}

	private void drawQuery(Multimap<String, Term> mapQueryResult) {
		// TODO Auto-generated method stub
		
	}

	private Multimap<String, Term> evaluateQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
