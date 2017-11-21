/*
 * Copyright 2017 Michael.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * Controller Class for the Tree Table
 * @author md
 */
public class Controller implements Initializable {

    @FXML TreeTableView<Mapper> table;
    @FXML TreeTableColumn<Mapper, String> c1;
    @FXML TreeTableColumn<Mapper, String> c2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*build objects*/
        List<Basket> objektliste = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objektliste.add(new Basket("Basket no" + i, Arrays.asList(
                    new Item[]{new Item("Item no 1", 1), new Item("Item no 2", 55), new Item("Item no 3", 99.9)}))
            );
        }

        /*build root node*/
        TreeItem<Mapper> rootNode = new TreeItem<>(new Mapper("Baskets"));
        table.setRoot(rootNode);
        table.showRootProperty().set(false);

        /*create mappers and add the nodes*/
        objektliste.stream().forEach((Basket b) -> {
            TreeItem<Mapper> node = new TreeItem<>();
            List<Mapper> nodesAlsMapper = new ArrayList<>();
            b.items.forEach(i -> nodesAlsMapper.add(new Mapper(i.name, i.price)));
            node.setValue(new Mapper(b.name, nodesAlsMapper));
            b.items.stream().forEach(item -> node.getChildren().add(new TreeItem<>(new Mapper(item.name, item.price))));
            rootNode.getChildren().add(node);
        });

        /*cell factories*/
        c1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mapper, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().name));
        c2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mapper, String> param) -> (param.getValue().getValue().subMappers == null)
                ? new ReadOnlyStringWrapper(param.getValue().getValue().value + "€")
                : new ReadOnlyStringWrapper(param.getValue().getValue().getValuesCombined() + "€")
        );

    }

    /**
     * Mapping Class to encapsule multiple objects into a shared table structure.
     */
    private class Mapper {

        String name;
        double value;
        /**
         * The single one hack to allow subnodes of different types.
         */
        List<Mapper> subMappers;

        public Mapper(String name) {
        }

        public Mapper(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public Mapper(String name, List<Mapper> subMappers) {
            this.name = name;
            this.subMappers = subMappers;
        }

        double getValuesCombined() {
            return subMappers.stream().mapToDouble(e -> e.value).sum();
        }
    }
}
