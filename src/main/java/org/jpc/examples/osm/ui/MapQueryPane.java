package org.jpc.examples.osm.ui;

import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import netscape.javascript.JSObject;

import org.jpc.commons.prologbrowser.ui.QueryBrowserPane;
import org.jpc.engine.profile.PrologEngineProfile;
import org.jpc.engine.profile.PrologEngineProfileFactory;
import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.driver.PrologEngineFactory;
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
import org.jpc.query.QueryListener;
import org.jpc.query.Solution;
import org.jpc.term.ListTerm;
import org.jpc.term.Term;
import org.jpc.util.PrologResourceLoader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.GsonBuilder;

public class MapQueryPane extends Region implements QueryListener {
	
	public static final String JAVA_SCRIPT_INTERFACE_VARIABLE = "java"; //the name of the javascript variable that will be created in the browser to refer to methods in this class
	public static final String NODE_VARIABLE_NAME = "Node";
	public static final String WAY_VARIABLE_NAME = "Way";
	
	private Executor mqExecutor;
	private QueryBrowserPane queryBrowser;
	private final Button loadOsmFileButton;
	private final ProgressIndicator loadOsmFileProgress;
	private final WebEngine webEngine;
	
	public MapQueryPane(final WebEngine webEngine, final QueryBrowserPane queryBrowser) {
		this.webEngine = webEngine;
		this.queryBrowser = queryBrowser;
		this.mqExecutor = queryBrowser.getExecutor();
		
		queryBrowser.queryListenersProperty().add(this);
		
		queryBrowser.getSettingsPane().getModel().addProfileFactory(new PrologEngineProfileFactory<PrologEngine>() {
			@Override
			public PrologEngineProfile createPrologEngineProfile(PrologEngineFactory prologEngineFactory) {
				return new PrologEngineProfile(prologEngineFactory) {
						@Override
						public void onCreate(PrologEngine prologEngine) {
							new PrologResourceLoader(prologEngine).logtalkLoad(MapQuery.LOADER_FILE);
						}
				};
			}
		});
		
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject win = (JSObject)webEngine.executeScript("window"); //this and the following instruction should be executed only after the web page has been completely loaded
					win.setMember(JAVA_SCRIPT_INTERFACE_VARIABLE, new BrowserInterface());
				}
			}
		});

		setPadding(new Insets(5, 20, 5, 20));
		
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
				File selectedFile = fc.showOpenDialog(MapQueryPane.this.getScene().getWindow());
				if(selectedFile != null) {
					file = selectedFile; //so next time the dialog will open in the same directory
					fileTextField.setText(selectedFile.getAbsolutePath());
				}
			}
		});

		loadOsmFileButton = new Button("Import");
		loadOsmFileButton.disableProperty().bind(Bindings.not(queryBrowser.getLogicConsolePane().getPrologEngineChoiceModel().selectedEngineAvailableProperty()));
		
		loadOsmFileProgress = new ProgressIndicator();
		loadOsmFileProgress.setVisible(false);
		
		loadOsmFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final String osmFile = fileTextField.getText().trim();
				if(osmFile.isEmpty())
					SimpleDialog.error("Please select a file with OSM data to load").showDialog();
				else {
					loadOsmFileProgress.setVisible(true);
					//loadOsmFileProgress.setProgress(0);
					loadOsmFileProgress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
					mqExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								new OsmDataLoader(queryBrowser.getCurrentPrologEngine()).load(osmFile);
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
		});
		
		HBox osmFileOptions = createHBox();
		HBox fileChooser = new HBox();
		fileChooser.getChildren().add(fileTextField);
		fileChooser.getChildren().add(browseFileButton);
		osmFileOptions.getChildren().addAll(new Label("OSM file:"), fileChooser, loadOsmFileButton, loadOsmFileProgress);
		queryBrowser.getLogicConsolePane().getChildren().add(osmFileOptions);
		
		getChildren().add(queryBrowser);
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

	private class BrowserInterface {
//		public void query(String queryString) {
//			ConsolePane.this.query(queryString);
//		}
	}

	private void drawSolution(ListMultimap<String, Term> mapQueryResult) {
		List<Term> nodeTerms = mapQueryResult.get(NODE_VARIABLE_NAME);
		if(nodeTerms == null)
			nodeTerms = new ArrayList<>();
			List<Term> wayTerms = mapQueryResult.get(WAY_VARIABLE_NAME);
		if(wayTerms == null)
			wayTerms = new ArrayList<>();
			
		List<Node> nodes = getOsmContext().fromTerm(new ListTerm(nodeTerms).asTerm());
		List<Way> ways = getOsmContext().fromTerm(new ListTerm(wayTerms).asTerm());

//		int numberNodes = nodes.size();
//		int numberWays = ways.size();
//		System.out.println("Number of nodes: " + numberNodes);
//		System.out.println("Number of ways: " + numberWays);
		
		
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

	private void addSolution(ListMultimap<String, Term> solutionsMultimap, List<Solution> solutions) {
		for(Solution solution : solutions) {
			addSolution(solutionsMultimap, solution);
		}
	}
	
	private void addSolution(ListMultimap<String, Term> solutionsMultimap, Solution solution) {
		for(Entry<String, Term> entry : solution.entrySet()) {
			solutionsMultimap.put(entry.getKey(), entry.getValue());
		}
	}
	
	public void stop() {
		queryBrowser.stop();
	}

	@Override
	public void onQueryReady() {
	}

	@Override
	public void onQueryOpened() {
	}

	@Override
	public void onQueryExhausted() {
	}

	@Override
	public void onQueryInProgress() {
	}

	@Override
	public void onQueryFinished() {
	}

	@Override
	public void onException(Exception e) {
	}

	@Override
	public void onNextSolutionFound(Solution solution) {
		ListMultimap<String, Term> solutionsMultimap = ArrayListMultimap.create();
		addSolution(solutionsMultimap, solution);
		drawSolution(solutionsMultimap);
	}

	@Override
	public void onSolutionsFound(List<Solution> solutions) {
		ListMultimap<String, Term> solutionsMultimap = ArrayListMultimap.create();
		addSolution(solutionsMultimap, solutions);
		drawSolution(solutionsMultimap);
	}

	@Override
	public void onQueryDisposed() {
	}

}
