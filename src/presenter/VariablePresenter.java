package presenter;

import javax.swing.*;

public class VariablePresenter {
    private final JComboBox<Integer> dropdown;
    private final JButton deleteButton;
    public VariablePresenter(JComboBox<Integer> dropdown, JButton deleteButton){
        this.dropdown = dropdown;
        this.deleteButton = deleteButton;
    }
    public void addVariable(){
        this.dropdown.addItem(dropdown.getItemCount());
        this.dropdown.setSelectedItem(dropdown.getItemCount() - 1);
        deleteButton.setEnabled(true);
    }

    public void removeVariable(){
        this.dropdown.removeItem(dropdown.getItemCount()-1);
        if (dropdown.getItemCount() == 1)
            deleteButton.setEnabled(false);
    }
}
