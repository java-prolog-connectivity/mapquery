package org.jpc.examples.osm.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
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
import netscape.javascript.JSObject;

import org.jpc.engine.prolog.PrologEngineConfiguration;
import org.jpc.examples.osm.MapQuery;
import org.jpc.examples.osm.OsmDataLoader;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.Osm;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.gsonconverters.CoordinateGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.NodeGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.OsmGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.WayGsonConverter;
import org.jpc.examples.osm.model.imp.OsmFragment;
import org.jpc.examples.osm.model.jpcconverters.TermToNodeConverter;
import org.jpc.examples.osm.model.jpcconverters.TermToWayConverter;
import org.jpc.query.Query;
import org.jpc.term.Term;
import org.jpc.util.LogicResourceLoader;
import org.jpc.util.PrologEngineManager;
import org.jpc.util.concurrent.JpcCallable;
import org.jpc.util.concurrent.JpcExecutor;
import org.jpc.util.concurrent.JpcRunnable;
import org.jpc.util.concurrent.OneThreadJpcExecutor;
import org.jpc.util.concurrent.ThreadLocalPrologEngine;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.gson.GsonBuilder;

public class LogicConsole extends GridPane {

	public static final Font TITLE_FONT = Font.font("Helvetica", FontWeight.NORMAL, 20); //TODO move this to a style file
	
	public static final String NODE_VARIABLE_NAME = "Node";
	public static final String WAY_VARIABLE_NAME = "Way";
	
	private JpcExecutor jpcExecutor;
	
	final Multimap<String, PrologEngineConfiguration> groupedEngineConfigurations;
	final ComboBox prologEnginesComboBox;
	final ComboBox bridgeLibraryComboBox;
	final Button startEngineButton;
	final ProgressIndicator startEngineProgress;
	final ProgressIndicator loadOsmFileProgress;
	
	//private OsmBrowser osmBrowser;
	final private WebEngine webEngine;
	
	public LogicConsole(final WebEngine webEngine) {
		this.webEngine = webEngine;
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject win = (JSObject)webEngine.executeScript("window"); //this and the following instruction should be executed only after the web page has been completely loaded
					win.setMember("java", new BrowserInterface());
				}
			}
		});
		
		PrologEngineManager manager = PrologEngineManager.getDefault();
		manager.register(PrologEngineManager.findConfigurations());
		groupedEngineConfigurations = manager.groupByPrologEngine();
		final Multiset<String> prologEnginesMultiset = groupedEngineConfigurations.keys();
		SortedSet<String> prologEngines = new TreeSet<>(Arrays.asList(prologEnginesMultiset.toArray(new String[]{})));
		
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
		
		prologEnginesComboBox.getItems().addAll(prologEngines);

		add(new Label("Engine:"), 0,1);
		add(prologEnginesComboBox, 1,1);
		add(new Label("Library:"), 2,1);
		add(bridgeLibraryComboBox, 3,1);
		
		startEngineButton = new Button("Start");
		startEngineProgress = new ProgressIndicator();
		
		startEngineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				initializeEngine();
			}
		});
		
		add(startEngineButton, 0,2);
		add(startEngineProgress, 1,2);
		startEngineProgress.setVisible(false);
		
		
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

		Button loadFileButton = new Button("Import");
		loadOsmFileProgress = new ProgressIndicator();
		
		loadFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final String osmFile = fileTextField.getText().trim();
				if(osmFile.isEmpty())
					SimpleDialog.error("Please select a file with OSM data to load").showDialog();
				else {
					if(jpcExecutor == null) {
						if(verifyPrologEngineSelection()) {
							initializeEngine();
						}

//						boolean resultInitialization;
//						try {
//							resultInitialization = initializeEngine().get(); //initializes the jpc executor and an associated logic engine
//						} catch (InterruptedException | ExecutionException e) {
//							throw new RuntimeException(e);
//						} 
//						if(!resultInitialization) {
//							SimpleDialog.error("Impossible to initialize correctly Prolog engine").showDialog();
//							if(jpcExecutor != null) {
//								jpcExecutor.shutdownNow();
//								jpcExecutor = null;
//							}
//						}
					}
					
					if(jpcExecutor != null) {
						loadOsmFileProgress.setVisible(true);
						//loadOsmFileProgress.setProgress(0);
						
						jpcExecutor.execute(new JpcRunnable() {
							@Override
							public void run() {
								try {
									new OsmDataLoader(getPrologEngine()).load(osmFile);
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											loadOsmFileProgress.setProgress(1);
										}
									});
								} catch(RuntimeException e) {
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											loadOsmFileProgress.setVisible(false);
										}
									});
									throw e; 
								}
							}
						});
					}
				}
			}
		});
		
		add(new Label("OSM file:"), 0,3);
		add(fileTextField, 1,3,2,1);
		add(browseFileButton, 3,3);
		add(loadFileButton, 0,4);
		add(loadOsmFileProgress, 1,4);
		loadOsmFileProgress.setVisible(false);
		//progress.setDisable(true);
		if(!prologEngines.isEmpty()) {
			//prologEnginesComboBox.set
			prologEnginesComboBox.setValue(prologEngines.iterator().next());
		} else {
			SimpleDialog.warning("Impossible to find available logic engines. The application will not work correctly.").showDialog();
		}
	}
	
	public JpcExecutor getJpcExecutor() {
		return jpcExecutor;
	}
	
	private Future<Boolean> initializeEngine() {
		PrologEngineConfiguration config = getSelectedConfiguration();
		jpcExecutor = new OneThreadJpcExecutor(config);
		//jpcExecutor = new JpcExecutor(new DirectExecutorService(), config);
		startEngineProgress.setVisible(true);

		return jpcExecutor.submit(new JpcCallable<Boolean>() {
			@Override
			public Boolean call() {
				try {
					ThreadLocalPrologEngine.setPrologEngine(getPrologEngine());
					boolean resourcesLoaded = new LogicResourceLoader(getPrologEngine()).logtalkLoad(MapQuery.LOADER_FILE);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							startEngineProgress.setProgress(1);
							disableEngineConfigurationOptions();
							enableQueryOptions();
						}
					});
					return resourcesLoaded;
				} catch(RuntimeException e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							startEngineProgress.setVisible(false);
						}
					});
					throw e; 
				}
			}
		});
	}
	
	private PrologEngineConfiguration getSelectedConfiguration() {
		String prologEngineName = (String)prologEnginesComboBox.getValue();
		Collection<PrologEngineConfiguration> configurations = groupedEngineConfigurations.get(prologEngineName);
		String libraryName = (String) bridgeLibraryComboBox.getValue();
		for(PrologEngineConfiguration config : configurations) {
			if(config.getLibraryName().equals(libraryName))
				return config;
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
		startEngineButton.setDisable(true);
		prologEnginesComboBox.setDisable(true);
		bridgeLibraryComboBox.setDisable(true);
	}
	
	public void disableQueryOptions() {
		webEngine.executeScript("g_disableQueryOptions()");
	}

	public void enableQueryOptions() {
		webEngine.executeScript("g_enableQueryOptions()");
	}
	
	private class BrowserInterface {
		public void query(String queryString) {
			LogicConsole.this.query(queryString);
		}
	}
	
	public void query(final String queryString) {
		System.out.println("message received");
		System.out.println(queryString);
		Query query;
		try {
			query = jpcExecutor.submit(new JpcCallable<Query>() {
				@Override
				public Query call() throws Exception {
					return getPrologEngine().query(queryString);
				}
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		ListMultimap<String, Term> mapQueryResult = query.allSolutionsMultimap();
		drawQuery(mapQueryResult);
	}

	private void drawQuery(ListMultimap<String, Term> mapQueryResult) {
		List<Term> nodeTerms = mapQueryResult.get(NODE_VARIABLE_NAME);
		if(nodeTerms == null)
			nodeTerms = new ArrayList<>();
			List<Term> wayTerms = mapQueryResult.get(WAY_VARIABLE_NAME);
		if(wayTerms == null)
			wayTerms = new ArrayList<>();
		
		List<Node> nodes = Lists.transform(nodeTerms, new TermToNodeConverter());
		List<Way> ways = Lists.transform(wayTerms, new TermToWayConverter());

		Osm osm = new OsmFragment(nodes, ways);
		
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(Coordinate.class, new CoordinateGsonConverter());
		gson.registerTypeAdapter(Node.class, new NodeGsonConverter());
		gson.registerTypeAdapter(Way.class, new WayGsonConverter());
		gson.registerTypeAdapter(OsmFragment.class, new OsmGsonConverter());
		
		//gson.setPrettyPrinting();
		
		String osmJson = gson.create().toJson(osm);
		System.out.println(osmJson);
		webEngine.executeScript("g_drawGeoJson("+osmJson+")");
//		int numberNodes = nodes.size();
//		int numberWays = ways.size();
//		System.out.println("Number of nodes: " + numberNodes);
//		System.out.println("Number of ways: " + numberWays);
		
	}



}
