package fr.hyper.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.joml.Matrix3f;


public class MatrixEditor extends JPanel implements CellEditorListener {
	public static final Dimension MINIMUM_SIZE = new Dimension(100, 200);

	public static final String[] COLUMN_NAMES = {"x", "y"};

	private JPanel pane;

	private Matrix3f matrix = new Matrix3f(),
			lastTransform = new Matrix3f();

	private JTable matrixTable = new JTable(new String[][] {{"1.0", "0.0"}, {"0.0", "1.0"}, {"0.0", "0.0"}}, COLUMN_NAMES);

	private JLabel error = new JLabel();

	private double timeLastChange = 0;

	public MatrixEditor() {
		this.setMinimumSize(MINIMUM_SIZE);
		this.setPreferredSize(MINIMUM_SIZE);
		this.setBorder(new TitledBorder("Matrice"));
		this.setLayout(new BorderLayout());

		this.pane = new JPanel();
		this.pane.setLayout(new BorderLayout());

		this.matrixTable.getCellEditor(0, 0).addCellEditorListener(this);

		this.error.setForeground(Color.RED);
		this.error.setHorizontalAlignment(JLabel.CENTER);

		this.pane.add(new JScrollPane(matrixTable));

		this.add(pane, BorderLayout.CENTER);
		this.add(error, BorderLayout.SOUTH);

		read();
	}

	private void read() {
		Float x1 = getValue((String) this.matrixTable.getValueAt(0, 0)), x2 = getValue((String)this.matrixTable.getValueAt(1, 0)), x3 = getValue((String)this.matrixTable.getValueAt(2, 0)),
				y1 = getValue((String)this.matrixTable.getValueAt(0, 1)), y2 = getValue((String)this.matrixTable.getValueAt(1, 1)), y3 = getValue((String)this.matrixTable.getValueAt(2, 1));
		error.setText("");

		lastTransform = new Matrix3f(matrix);
		if(x1 != null)
			matrix.m00(x1);

		if(x2 != null)
			matrix.m01(x2);
		
		if(x3 != null)
			matrix.m02(x3);

		if(y1 != null)
			matrix.m10(y1);

		if(y2 != null)
			matrix.m11(y2);

		if(y3 != null)
			matrix.m12(y3);

		this.timeLastChange = (double)System.currentTimeMillis()/1000d;

		write();
	}

	private void write() {
		matrixTable.setValueAt(String.valueOf(matrix.m00()), 0, 0);
		matrixTable.setValueAt(String.valueOf(matrix.m10()), 0, 1);
		matrixTable.setValueAt(String.valueOf(matrix.m01()), 1, 0);
		matrixTable.setValueAt(String.valueOf(matrix.m11()), 1, 1);
		matrixTable.setValueAt(String.valueOf(matrix.m02()), 2, 0);
		matrixTable.setValueAt(String.valueOf(matrix.m12()), 2, 1);
	}

	public Matrix3f getMatrix() {
		return matrix;
	}

	public Matrix3f getLastMatrix() {
		return lastTransform;
	}

	public double getLastChangeTime() {
		return timeLastChange;
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		read();
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
		write();
	}

	private Float getValue(String value) {
		try {
			return new Float(value);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			if(error.getText().isEmpty())
				error.setText("Entrée non reconnue : " + e.getLocalizedMessage());
			else
				error.setText(error.getText() + ", Entrée non reconnue : " + e.getLocalizedMessage());
			System.out.println(error.getText());
			return null;
		}
	}
}
