
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.HashSet;

import javax.swing.*;

class Panel extends JPanel {

	Panel() {

	}

	public static void draw() {
		LocalDate sd = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
		int startDay = sd.getDayOfWeek().getValue();

		int day = 1;

		for (int i = 1; i <= 42; i++) {
			if (i < startDay || i > LocalDate.now().lengthOfMonth() + startDay - 1) {
				JButton b = new JButton();
				EventCalendar.p.add(b);
			}

			else {
				JButton b = new JButton(day + "");

				if (day == LocalDate.now().getDayOfMonth()) {

					b.setBackground(Color.YELLOW);
				}
				if (i % 7 == 0) {

					b.setBackground(Color.RED);
				}
				EventCalendar.p.add(b);

				day++;
			}

		}
	}

}

public class EventCalendar implements ActionListener {
	static JFrame f = new JFrame();
	static JComboBox years;
	static JComboBox months;
	static JButton submit;
	static Panel p;

	// Event
	static JTextField eventName;
	static JTextArea description;
	static JButton addEvent;
	static JButton selectedDate;
	static String date;
	static JDialog dialog;
	static HashSet<String> eventSet;
	// Data base
	static Connection con;

	// static JTextField ;
	// static JTextArea;
	public EventCalendar() {
		String year_Type[] = { "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024" };
		String month_Type[] = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
				"OCTOBER", "NOVEMBER", "DECEMBER" };
		years = new JComboBox<>(year_Type);
		years.setSelectedItem(LocalDate.now().getYear() + "");
		months = new JComboBox<>(month_Type);
		months.setSelectedItem(LocalDate.now().getMonth() + "");
		years.setBounds(100, 50, 100, 20);
		months.setBounds(250, 50, 100, 20);
		submit = new JButton("Submit");
		submit.setBounds(400, 50, 100, 20);
		f.add(submit);

		//
		JLabel mon = new JLabel("MON");
		mon.setBounds(120, 120, 85, 30);
		f.add(mon);
		JLabel tue = new JLabel("TUE");
		tue.setBounds(210, 120, 85, 30);
		f.add(tue);
		JLabel wed = new JLabel("WED");
		wed.setBounds(300, 120, 85, 30);
		f.add(wed);
		JLabel thur = new JLabel("THUR");
		thur.setBounds(390, 120, 85, 30);
		f.add(thur);
		JLabel fri = new JLabel("FRI");
		fri.setBounds(480, 120, 85, 30);
		f.add(fri);
		JLabel sat = new JLabel("SAT");
		sat.setBounds(570, 120, 85, 30);
		f.add(sat);
		JLabel sun = new JLabel("SUN");
		sun.setBounds(660, 120, 85, 30);
		f.add(sun);
		// When opens the Application remainder will visible if any event did set

		eventSet = new HashSet<>();

		try {
			PreparedStatement pstmt = con.prepareStatement("select  * from events");
			ResultSet rs = pstmt.executeQuery();
			String d = LocalDate.now().toString();
			while (rs.next()) {
				if (rs.getString(1).equals(d)) {
					JOptionPane.showMessageDialog(f, "Remainder  : " + rs.getString(1) + "\n            "
							+ rs.getString(2) + "\n" + rs.getString(3));
				}
				eventSet.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

		submit.addActionListener(this);

		f.setLayout(null);
		f.add(years);
		f.add(months);
		f.setSize(800, 800);

		p = new Panel();
		p.setLayout(new GridLayout(6, 7, 2, 2));
		p.setBounds(100, 150, 600, 400);
		p.draw();

		Component comp[] = p.getComponents();
		for (Component c : comp) {
			JButton b = (JButton) c;
			b.addActionListener(this);
		}

		f.add(p);

		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == EventCalendar.submit) {
			f.remove(p);
			p = new Panel();
			p.setLayout(new GridLayout(6, 7, 2, 2));
			p.setBounds(100, 150, 600, 400);
			String y = (String) years.getSelectedItem();
			// System.out.println(y);
			Month m = Month.valueOf((String) months.getSelectedItem());
			String month = "";
			if (m.getValue() < 10) {
				month = 0 + "" + m.getValue();
			} else
				month = m.getValue() + "";
			LocalDate d = LocalDate.parse(y + "-" + month + "-01");

			int startDay = d.getDayOfWeek().getValue();
			int day = 1;
			for (int i = 1; i <= 42; i++) {

				if (i < startDay || i > d.lengthOfMonth() + startDay - 1) {
					JButton b = new JButton();
					p.add(b);
				} else {

					JButton b = new JButton(day + "");

					if ((Integer.parseInt(y) == LocalDate.now().getYear())
							&& (Integer.parseInt(month) == LocalDate.now().getMonthValue())
							&& (day == LocalDate.now().getDayOfMonth())) {
						b.setBackground(Color.YELLOW);
					}
					if (i % 7 == 0) {
						b.setBackground(Color.RED);
					}
					String text = b.getText();
					if (text.length() < 2)
						text = "0" + text;

					if (eventSet.contains(y + "-" + month + "-" + text)) {

						System.out.println("yes");
						b.setBackground(Color.orange);
					}
					p.add(b);
					day++;
				}

			}

			// Event display and add

			Component comp[] = p.getComponents();
			for (Component c : comp) {
				JButton b = (JButton) c;
				b.addActionListener(this);
			}

			f.add(p);
			f.revalidate();
			f.repaint();

		}
		Component comp[] = p.getComponents();

		for (Component c : comp) {
			JButton b = (JButton) c;
			if (e.getSource() == b) {
				selectedDate = b;
				dialog = new JDialog();
				dialog.setTitle("Add Event");
				dialog.setSize(300, 300);
				dialog.setLocationRelativeTo(null);

				JPanel panel = new JPanel();

				JLabel label1 = new JLabel("EventName");
				label1.setBounds(0, 10, 100, 40);
				Font boldFont = new Font("Arial", Font.BOLD, 14);
				label1.setFont(boldFont);

				JLabel label2 = new JLabel("Description");
				label2.setBounds(0, 60, 100, 40);
				label2.setFont(boldFont);

				eventName = new JTextField();
				eventName.setBounds(100, 10, 150, 40);
				description = new JTextArea(4, 4);
				description.setBounds(100, 60, 150, 80);
				addEvent = new JButton("Add");
				addEvent.setBounds(100, 150, 150, 40);
				addEvent.addActionListener(this);

				try {

					String y = (String) years.getSelectedItem();
					Month m = Month.valueOf((String) months.getSelectedItem());
					String d = y + "/" + m.getValue() + "/" + selectedDate.getText();
					DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/M/d");
					LocalDate ld = LocalDate.parse(d, f);
					PreparedStatement pstmt = con.prepareStatement("select * from events where date=?");
					pstmt.setString(1, ld.toString());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						eventName.setText(rs.getString(2));
						description.setText(rs.getString(3));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				panel.add(label2);
				panel.add(label1);
				panel.add(eventName);
				panel.add(description);
				panel.add(addEvent);
				panel.setLayout(null);
				dialog.add(panel);

				dialog.setModal(true);
				dialog.setVisible(true);

			}

		}
		if (e.getSource() == addEvent) {
			String y = (String) years.getSelectedItem();
			Month m = Month.valueOf((String) months.getSelectedItem());

			String d = y + "/" + m.getValue() + "/" + selectedDate.getText();
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/M/d");
			LocalDate ld = LocalDate.parse(d, f);
			// System.out.println(ld.toString());

			String evName = eventName.getText();
			String descri = description.getText();
			try {
				PreparedStatement pstmt = con.prepareStatement("insert into events values(?,?,?)");
				pstmt.setString(1, ld.toString());
				pstmt.setString(2, evName);
				pstmt.setString(3, descri);
				int res = pstmt.executeUpdate();
				{
					if (res == 1) {
						eventSet.add(ld.toString());
						dialog.dispose();
						submit.doClick();
					}

				}

			} catch (Exception e1) {
				try {
					PreparedStatement pstmt = con
							.prepareStatement("update events set eventName=?,description=? where date=?");
					pstmt.setString(1, evName);
					pstmt.setString(2, descri);
					pstmt.setString(3, ld.toString());
					int res = pstmt.executeUpdate();
					if (res == 1) {
						dialog.dispose();
					}
				}

				catch (Exception e2) {
					System.out.println(e2);
				}
				System.out.println(e1);
			}

		}

	}

	public static void main(String[] args) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/calendar", "root", "Balaji@3456");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		new EventCalendar();
	}
}
