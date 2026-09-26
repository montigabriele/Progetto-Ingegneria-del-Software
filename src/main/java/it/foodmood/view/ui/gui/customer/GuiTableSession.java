package it.foodmood.view.ui.gui.customer;

import it.foodmood.bean.EnterTableSessionBean;
import it.foodmood.bean.TableSessionBean;
import it.foodmood.controller.TableSessionController;
import it.foodmood.exception.TableException;
import it.foodmood.exception.TableSessionException;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GuiTableSession extends BaseGui{
    
    @FXML private Button btnNext;

    @FXML private Label errorMessageLabel;

    @FXML private TextField tfTableNumber;

    private GuiRouter router;

    private final TableSessionController controller = new TableSessionController();

    public void setRouter(GuiRouter router){
        this.router = router;
    }

    @FXML
    private void onHomePage(){
        errorMessageLabel.setText("");

        EnterTableSessionBean enterTableSessionBean = new EnterTableSessionBean();

        enterTableSessionBean.setTableNumber(tfTableNumber.getText().trim());

        try {

            int tableId = enterTableSessionBean.getTableId();

            TableSessionBean tableSessionBean = controller.enterSession(tableId);
            
            router.showHomeCustumerView(tableSessionBean);

        } catch (IllegalArgumentException e){
            errorMessageLabel.setText(e.getMessage());
        } catch (TableException e){
            errorMessageLabel.setText(e.getMessage());
        } catch (TableSessionException e){
            errorMessageLabel.setText(e.getMessage());
        }
    }
}
