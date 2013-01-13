package org.jpc.examples.osm.ui;

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
import org.jpc.util.PrologEngineManager;
import org.jpc.util.LogicResourceLoader;
import org.jpc.util.concurrent.ThreadLocalPrologEngine;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

public class LogicConsole extends GridPane {

	public static final Font TITLE_FONT = Font.font("Helvetica", FontWeight.NORMAL, 20); //TODO move this to a style file
	
	public static final String NODE_VARIABLE_NAME = "Node";
	public static final String WAY_VARIABLE_NAME = "Way";
	
	
	final Multimap<String, PrologEngineConfiguration> groupedEngineConfigurations;
	final ComboBox prologEnginesComboBox;
	final ComboBox bridgeLibraryComboBox;
	private OsmBrowser osmBrowser;
	private WebEngine webEngine;
	private PrologEngine prologEngine;
	
	public LogicConsole(WebEngine webEngine) {
		this.webEngine = webEngine;
		PrologEngineManager manager = PrologEngineManager.getDefault();
		manager.register(PrologEngineManager.findConfigurations());
		groupedEngineConfigurations = manager.groupByPrologEngine();
		final Multiset<String> prologEnginesMultiset = groupedEngineConfigurations.keys();
		Set<String> prologEngines = new HashSet(Arrays.asList(prologEnginesMultiset.toArray()));
		
		//setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));
		
		Text logicConsoleTitle = new Text("Logic Console settings");
		logicConsoleTitle.setFont(TITLE_FONT);
		add(logicConsoleTitle, 0, 0, 2, 1);
		
		
		prologEnginesComboBox = new ComboBox();
		bridgeLibraryComboBox = new ComboBox();
		
		prologEnginesComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.equals(oldValue)) {
					Set<String> bridgeLibraries = new HashSet<>();
					String selectedPrologEngine = (String) prologEnginesComboBox.getValue();
					Collection<PrologEngineConfiguration> prologEngineConfigurations = groupedEngineConfigurations.get(selectedPrologEngine);
					for(PrologEngineConfiguration prologEngineConfiguration : prologEngineConfigurations) {
						bridgeLibraries.add(prologEngineConfiguration.getLibraryName());
					}
					bridgeLibraryComboBox.getItems().clear();
					bridgeLibraryComboBox.getItems().addAll(bridgeLibraries);
					if(!bridgeLibraries.isEmpty()) {
						bridgeLibraryComboBox.setValue(bridgeLibraries.iterator().next());
					}	
				}
			}
		});
		
		//prologEnginesComboBox.getItems().addAll(lprologEngines

		add(new Label("Prolog engine:"), 0,1);
		add(prologEnginesComboBox, 1,1);
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
					if(prologEngine != null || initializePrologEngine()) {
						progress.setVisible(true); //this is affecting the progress bar only after the handle method has returned
						//progress.setProgress(0);
						try {
							new OsmDataLoader(prologEngine).load(osmFile);
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
		if(!prologEngines.isEmpty()) {
			prologEnginesComboBox.setValue(prologEngines.iterator().next());
		} else {
			SimpleDialog.warning("Impossible to find available logic engines. The application will not work correctly.").showDialog();
		}
	}
	
	private boolean initializePrologEngine() {
		if(verifyPrologEngineSelection()) {
			prologEngine = getSelectedPrologEngine();
			ThreadLocalPrologEngine.setPrologEngine(prologEngine);
			Thread t = new Thread() {
				@Override public void run() {
					System.out.println(ThreadLocalPrologEngine.getPrologEngine() != null);
					ThreadLocalPrologEngine.setPrologEngine(prologEngine);
					System.out.println(ThreadLocalPrologEngine.getPrologEngine() != null);
				}
			};
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			
			new LogicResourceLoader(prologEngine).logtalkLoad(MapQuery.LOADER_FILE);
			disableEngineConfigurationOptions();
			return true;
		} else
			return false;
	}
	
	private PrologEngine getSelectedPrologEngine() {
		String prologEngineName = (String)prologEnginesComboBox.getValue();
		Collection<PrologEngineConfiguration> configurations = groupedEngineConfigurations.get(prologEngineName);
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
	private boolean verifyPrologEngineSelection() {
		if(((String)prologEnginesComboBox.getValue()).trim().isEmpty() || ((String)bridgeLibraryComboBox.getValue()).trim().isEmpty()) {
			SimpleDialog.error("Please select a logic engine and a bridge library.").showDialog();
			return false;
		} else
			return true;
	}
	
	public void disableEngineConfigurationOptions() {
		prologEnginesComboBox.setDisable(true);
		bridgeLibraryComboBox.setDisable(true);
	}
	
	public void disableQueryOptions() {
		webEngine.executeScript("g_disableQueryOptions()");
	}

	public void enableQueryOptions() {
		webEngine.executeScript("g_enableQueryOptions()");
	}
	
	public void query(String queryString) {
		Multimap<String, Term> mapQueryResult = prologEngine.query(queryString).allSolutionsMultimap();
		drawQuery(mapQueryResult);
	}

	private void drawQuery(Multimap<String, Term> mapQueryResult) {
		Collection<Term> nodeTerms = mapQueryResult.get(NODE_VARIABLE_NAME);
		Collection<Term> wayTerms = mapQueryResult.get(WAY_VARIABLE_NAME);
		
		int numberNodes = nodeTerms!=null?nodeTerms.size():0;
		int numberWays = nodeTerms!=null?wayTerms.size():0;
		
		System.out.println("Number of nodes: " + numberNodes);
		System.out.println("Number of ways: " + numberWays);
	}



}
