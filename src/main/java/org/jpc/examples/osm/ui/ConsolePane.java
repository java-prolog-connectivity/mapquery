package org.jpc.examples.osm.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import org.jpc.util.concurrent.JpcCallable;
import org.jpc.util.concurrent.JpcExecutor;
import org.jpc.util.concurrent.JpcRunnable;
import org.jpc.util.concurrent.OneThreadJpcExecutor;
import org.jpc.util.concurrent.ThreadLocalPrologEngine;
import org.jpc.util.ui.PrologConfigurationChooserPane;
import org.jpc.util.ui.QueryPane;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;

public class ConsolePane extends VBox {
	
	public static final String JAVA_SCRIPT_INTERFACE_VARIABLE = "java"; //the name of the javascript variable that will be created in the browser to refer to methods in this class
	public static final String NODE_VARIABLE_NAME = "Node";
	public static final String WAY_VARIABLE_NAME = "Way";
	
	private JpcExecutor jpcExecutor;
	private final Button loadOsmFileButton;
	private final ProgressIndicator loadOsmFileProgress;
	private final Button startEngineButton;
	private final ProgressIndicator startEngineProgress;
	private final PrologConfigurationChooserPane configurationChooserPane;
	private final QueryPane queryPane;
	private final WebEngine webEngine;
	
	public ConsolePane(final WebEngine webEngine) {
		this.webEngine = webEngine;
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject win = (JSObject)webEngine.executeScript("window"); //this and the following instruction should be executed only after the web page has been completely loaded
					win.setMember(JAVA_SCRIPT_INTERFACE_VARIABLE, new BrowserInterface());
				}
			}
		});

		//setAlignment(Pos.CENTER);
		//setHgap(10);
		//setVgap(10);
		setPadding(new Insets(5, 20, 5, 20));
		
		Text logicConsoleTitle = new Text("Prolog Console settings");
		logicConsoleTitle.setFont(DefaultStyle.TITLE_FONT);
		getChildren().add(createTitleHBox(logicConsoleTitle));
		
		HBox configElements = createHBox();
		configurationChooserPane = new PrologConfigurationChooserPane();
		configElements.getChildren().add(configurationChooserPane);

		
		
		startEngineButton = new Button("Start");
		startEngineProgress = new ProgressIndicator();
		startEngineProgress.setVisible(false);

		startEngineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				initializeEngine();
			}
		});

		configElements.getChildren().addAll(startEngineButton, startEngineProgress);
		
//		configElements.getChildren().add(startEngineButton);
//		configElements.getChildren().add(startEngineProgress);
		
		getChildren().add(configElements);
		

		
		
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
	            	System.out.println(file.canRead());
	            	fc.setInitialDirectory(file);
	            }

				File selectedFile = fc.showOpenDialog(ConsolePane.this.getScene().getWindow());
				if(selectedFile != null) {
					file = selectedFile; //so next time the dialog will open in the same directory
					fileTextField.setText(selectedFile.getAbsolutePath());
				}
			}
			
		});

		loadOsmFileButton = new Button("Import");
		loadOsmFileProgress = new ProgressIndicator();
		loadOsmFileProgress.setVisible(false);
		
		loadOsmFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final String osmFile = fileTextField.getText().trim();
				if(osmFile.isEmpty())
					SimpleDialog.error("Please select a file with OSM data to load").showDialog();
				else {
					if(jpcExecutor == null) {
						if(configurationChooserPane.verifyPrologEngineSelection()) {
							initializeEngine();
						} else {
							SimpleDialog.error("Please select a logic engine and a bridge library.").showDialog();
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
		
		HBox osmFileOptions = createHBox();
		osmFileOptions.getChildren().add(new Label("OSM file:"));
		
		HBox fileChooser = new HBox();
		fileChooser.getChildren().add(fileTextField);
		fileChooser.getChildren().add(browseFileButton);
		osmFileOptions.getChildren().add(fileChooser);
		osmFileOptions.getChildren().add(loadOsmFileButton);
		osmFileOptions.getChildren().add(loadOsmFileProgress);
		getChildren().add(osmFileOptions);

		
		
		queryPane = new QueryPane();
		getChildren().add(createHBox(queryPane));
		
		
		queryPane.allSolutionsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String queryText = queryPane.queryText.getText();
				query(queryText);
			}
		});
	}
	
	public HBox createTitleHBox(Text title) {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10,0,10,0));
		hBox.getChildren().add(title);
		return hBox;
	}
	
	public HBox createHBox(javafx.scene.Node ...children) {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(0,10,0,10));
		hBox.setSpacing(10);
		hBox.getChildren().addAll(children);
		return hBox; 
	}
	
	public JpcExecutor getJpcExecutor() {
		return jpcExecutor;
	}
	
	private Future<Boolean> initializeEngine() {
		PrologEngineConfiguration config = configurationChooserPane.getSelectedConfiguration();
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
	

	
	public void disableEngineConfigurationOptions() {
		startEngineButton.setDisable(true);
		configurationChooserPane.disableEngineConfigurationOptions();
	}
	
	public void disableQueryOptions() {
		queryPane.disable();
	}

	public void enableQueryOptions() {
		queryPane.enable();
	}
	
	private class BrowserInterface {
		public void query(String queryString) {
			ConsolePane.this.query(queryString);
		}
	}
	
	public void query(final String queryString) {
		try {
			jpcExecutor.submit(new JpcCallable<ListMultimap<String, Term>>() {
				@Override
				public ListMultimap<String, Term> call() throws Exception {
					Query query = getPrologEngine().query(queryString);
					ListMultimap<String, Term> mapQueryResult = query.allSolutionsMultimap();
					drawQuery(mapQueryResult);
					return mapQueryResult;
				}
			});
		} catch (RuntimeException e) {
			throw e;
		}
		
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

		int numberNodes = nodes.size();
		int numberWays = ways.size();
		System.out.println("Number of nodes: " + numberNodes);
		System.out.println("Number of ways: " + numberWays);
		
		
		Osm osm = new OsmFragment(nodes, ways);
		
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(Coordinate.class, new CoordinateGsonConverter());
		gson.registerTypeAdapter(Node.class, new NodeGsonConverter());
		gson.registerTypeAdapter(Way.class, new WayGsonConverter());
		gson.registerTypeAdapter(OsmFragment.class, new OsmGsonConverter());
		
		//gson.setPrettyPrinting();
		
		final String osmJson = gson.create().toJson(osm);
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println(osmJson);
				System.out.println("preparing to draw...");
				webEngine.executeScript("g_drawGeoJson("+osmJson+")");
				System.out.println("done drawing! ...");
			}
			
		});
		
	}

	public void freeResources() {
		if(jpcExecutor != null)
			jpcExecutor.shutdownNow();
	}

}
