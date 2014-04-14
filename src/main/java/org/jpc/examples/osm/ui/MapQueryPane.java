package org.jpc.examples.osm.ui;

import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.jpc.commons.prologbrowser.ui.QueryBrowserPane;
import org.jpc.engine.profile.PrologEngineProfile;
import org.jpc.engine.profile.PrologEngineProfileFactory;
import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.driver.PrologEngineFactory;
import org.jpc.examples.osm.MapQuery;
import org.jpc.examples.osm.OsmDataLoader;
import org.jpc.query.QueryListener;
import org.jpc.query.Solution;
import org.jpc.util.ConversionUtil;
import org.jpc.util.engine.PrologResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapQueryPane extends Region implements QueryListener {
	
	Logger logger = LoggerFactory.getLogger(MapQueryPane.class);
	
	private Executor mqExecutor;
	private QueryBrowserPane queryBrowser;
	private final Button loadOsmFileButton;
	private final ProgressIndicator loadOsmFileProgress;
	private final MapBrowser mapBrowser;
	
	public MapQueryPane(final MapBrowser mapBrowser, final QueryBrowserPane queryBrowser) {
		this.mapBrowser = mapBrowser;
		this.queryBrowser = queryBrowser;
		this.mqExecutor = queryBrowser.getExecutor();
		
		queryBrowser.queryListenersProperty().add(this);
		
		queryBrowser.getSettingsPane().getModel().addProfileFactory(new PrologEngineProfileFactory<PrologEngine>() {
			@Override
			public PrologEngineProfile createPrologEngineProfile(PrologEngineFactory prologEngineFactory) {
				return new PrologEngineProfile(prologEngineFactory) {
						@Override
						public void onCreate(PrologEngine prologEngine) {
							new PrologResourceLoader(prologEngine).logtalkLoad(MapQuery.LOADER_FILE); //NOTE: Uncomment this to avoid specifying in the GUI the Logtalk entry file for the map example.
						}
				};
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
	            	//System.out.println(file.canRead());
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
		mapBrowser.draw(ConversionUtil.toObjectMap(solution, getOsmContext()));
	}

	@Override
	public void onSolutionsFound(List<Solution> solutions) {
		mapBrowser.draw(ConversionUtil.toObjectMapList(solutions, getOsmContext()));
	}


	@Override
	public void onQueryDisposed() {
	}

}
