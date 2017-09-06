package me.logans.GroupGenerator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.Observable;

public class Controller {

    @FXML MenuBar menuBar;
    @FXML TableView table;
    @FXML FlowPane pane;
    @FXML TableColumn personCol;
    @FXML TableColumn groupCol;
    @FXML FlowPane buttonPane;
    @FXML TextField groupNumberInput;
    @FXML Button groupSubmit;

    @FXML
    private void initialize() {
        menuBar.prefWidthProperty().bind(pane.widthProperty());
        table.prefWidthProperty().bind(pane.widthProperty());
        table.prefHeightProperty().bind(pane.heightProperty().subtract(menuBar.prefHeightProperty()).subtract(buttonPane.prefHeightProperty()));
        buttonPane.prefWidthProperty().bind(pane.widthProperty());
        groupNumberInput.prefWidthProperty().bind(buttonPane.widthProperty().subtract(groupSubmit.widthProperty()));
        table.getSelectionModel().setCellSelectionEnabled(true);

        personCol.setCellFactory(TextFieldTableCell.forTableColumn());
        personCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        groupCol.setCellFactory(TextFieldTableCell.forTableColumn());

        addRow();
    }


    @FXML
    private void keyPressed(KeyEvent event) {

            if( event.getCode() == KeyCode.ENTER) {
                //event.consume(); // don't consume the event or else the values won't be updated;
                return;
            }

            // switch to edit mode on keypress, but only if we aren't already in edit mode
            if(table.getEditingCell() == null) {
                if( event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                    TablePosition focusedCellPosition = table.getFocusModel().getFocusedCell();
                    table.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
                }
        }
    }

    @FXML
    private void keyReleased(KeyEvent event) {
        if( event.getCode() == KeyCode.ENTER) {
            // move focus & selection
            // we need to clear the current selection first or else the selection would be added to the current selection since we are in multi selection mode
            TablePosition pos = table.getFocusModel().getFocusedCell();

            if (pos.getRow() == -1) {
                table.getSelectionModel().select(0);
            }
            // add new row when we are at the last row
            else if (pos.getRow() == table.getItems().size() -1) {
                addRow();
            }
            // select next row, but same column as the current selection
            else if (pos.getRow() < table.getItems().size() -1) {
                table.getSelectionModel().clearAndSelect( pos.getRow() + 1, pos.getTableColumn());
            }
        }

    }

    @FXML
    private void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TextFieldTableCell targetCell = (TextFieldTableCell) event.getTarget();

            if (targetCell.getTableRow().getIndex() >= table.getItems().size()) {
                addRow();
            } else {
                table.edit(targetCell.getTableRow().getIndex(), targetCell.getTableColumn());
            }
        }
    }

    @FXML
    private void groupPeople() {
        try {
            int groups = Integer.parseInt(groupNumberInput.getText());

            // remove any empty cells

            if (groups > table.getItems().size()) {
                // too many people for that amount of groups.
                return;
            }

            ArrayList people = new ArrayList(table.getItems());

            int groupNum = 1;

            while (people.size() != 0) {
                int personIndex = (int) (Math.random() * people.size());

                Person p = (Person) people.get(personIndex);
                p.setGroup(Integer.toString(groupNum));
                people.remove(p);

                groupNum = groupNum % groups + 1;
            }

        } catch (NumberFormatException e) {
            // NaN
        }
    }

    public void addRow() {

        // get current position
        TablePosition pos = table.getFocusModel().getFocusedCell();

        // clear current selection
        table.getSelectionModel().clearSelection();

        // create new record and add it to the model
        Person person = new Person();
        table.getItems().add(person);

        // get last row
        int row = table.getItems().size() - 1;
        table.getSelectionModel().select( row, pos.getTableColumn());

        // scroll to new row
        table.scrollTo(person);

    }
}
