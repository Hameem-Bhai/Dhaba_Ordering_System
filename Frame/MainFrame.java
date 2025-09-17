//step 0
package Frame;
import Entity.*;
import java.awt.*; //package for font & coloring
import java.awt.event.*; //package for GUI components
import java.util.ArrayList; //package for interfaces
import java.util.List;
import javax.swing.*;

//step 1
public class MainFrame extends JFrame implements MouseListener, ActionListener
{
	//step 2
	private Font f1, f2, f3, f4; // f1-title, f2-button, f3-label, f4-text
	private Color c1, c2, c3, c4, c5; // c1-primary, c2-bg, c3-panel, c4-header, c5-text
	private JPanel panel1, panel2, panel3, panel4, panel5; // main, menu-left, order-center, discount-top, customer-right
	private JLabel label1, label2, label3, label4, label5, label6; // labels: discount, name, gender, age, status, logo
	private JTextField tf1, tf2; // tf1-name, tf2-discount
	private JTextField tfCash; // cash given
	private JButton bt1, bt2, bt3, bt4, bt5; // bt1-ShowTotal, bt2-ApplyDiscount, bt3-RemoveLast, bt4-Clear, bt5-Confirm
	private JRadioButton rb1, rb2, rb3, rb4; // gender radios
	private ButtonGroup bg1; // gender group
	private JComboBox<Integer> jb1; // age dropdown
	private JTextArea ta1, ta2; // ta1-summary, ta2-receipt
	private ImageIcon img1; // logo
	private Order currentOrder; // current order
	private Customer customer; // current customer
	private MenuManager menuManager; // menu manager
	private OrderService orderService; // order service

	// VARIABLE LEGEND (for easy editing)
	// Fonts: f1-title, f2-button, f3-label, f4-text
	// Colors: c1-primary, c2-background, c3-panel, c4-header, c5-text
	// Panels: panel1-main, panel2-menu, panel3-order, panel4-discount, panel5-customer
	// Fields: tf1-customerName, tf2-discountPercent
	// Buttons: bt1-showTotal, bt2-applyDiscount, bt3-removeLast, bt4-clearOrder, bt5-confirmOrder
	// Radios: rb1-male, rb2-female, rb3-other, rb4-monkey
	// Combo: jb1-age
	// TextAreas: ta1-orderSummary, ta2-receipt
	
	//step 4
	public MainFrame()
	{
		//step 4(a)
		super("Hameem Dhaba Ordering System");
		super.setBounds(100, 50, 1400, 800); //(x,y,width, height)
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		
		//step 4(b)
		panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		c2 = new Color(212, 175, 55); //background color
		panel1.setBackground(c2);
		
		//step 4(d)
		f1 = new Font("Copperplate", Font.BOLD, 19); //title font
		f2 = new Font("Segoe UI Semibold", Font.BOLD, 15); //button font
		f3 = new Font("Tahoma", Font.BOLD, 15); //label font
		f4 = new Font("Georgia", Font.PLAIN, 14); //text font
		
		c1 = new Color(94, 58, 47); //primary color
		c3 = new Color(222, 170, 100); //secondary color
		c4 = new Color(15, 15, 25); //accent color
		c5 = new Color(128, 0, 32); //text color
		
		// STEP: Initialize OOP components (Managers, Service, Models)
		try {
			menuManager = new MenuManager();
			orderService = new OrderServiceImpl(menuManager);
			customer = new Customer();
			currentOrder = orderService.createOrder(customer);
			DataManager.ensureDataDirectory();
		} catch (Exception e) {
			label5 = new JLabel(); // ensure status exists
			label5.setText("Init error: " + e.getMessage());
		}
		
		// STEP: Create menu panel (left side)
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(5, 6, 2, 2));
		panel2.setBackground(c3);
		panel2.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(c1, 3), 
			"Dhaba Menu", 
			javax.swing.border.TitledBorder.CENTER, 
			javax.swing.border.TitledBorder.TOP, 
			f1, 
			c1
		));
		
        // STEP: Create menu buttons
java.util.List<Entity.MenuItem> items = menuManager.getAvailableItems();
for (Entity.MenuItem item : items) {
    String key = item.getName().toLowerCase().replace(" ", "_") + ".jpg";
    ImageIcon itemIcon = loadIcon(key, 50, 50); // Use local variable, not img1
    
    String label = "<html><div style='text-align:center; font-size:12px; color:#800020;'>" + 
                  item.getName() + "-" + String.format("%.2f", item.getPrice()) + "৳</div></html>";
    
    JButton itemButton = new JButton(label, itemIcon); 
    itemButton.setFont(new Font("Garamond", Font.BOLD, 10));
    itemButton.setBackground(Color.PINK);
    itemButton.setForeground(new Color(74, 44, 42));
    itemButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(c1, 2),
        BorderFactory.createEmptyBorder(1, 0, 0, 0)
    ));
    itemButton.setFocusPainted(false);
    itemButton.setHorizontalTextPosition(SwingConstants.CENTER);
    itemButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    itemButton.setIconTextGap(5);
    itemButton.setPreferredSize(new Dimension(80, 40));
    
    // CRITICAL: Store the MenuItem object in the button
    itemButton.putClientProperty("menuItem", item);
    
    itemButton.addActionListener(this);
    panel2.add(itemButton);
}



		
		// STEP: Create order panel (center)
		panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.setBackground(c3);
		panel3.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(c1, 3), 
			"Customer Order", 
			javax.swing.border.TitledBorder.CENTER, 
			javax.swing.border.TitledBorder.TOP, 
			f1, 
			c1
		));
		
		// STEP: Discount section
		panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
		panel4.setOpaque(false);
		label1 = new JLabel("Discount (%):");
		label1.setFont(f3);
		panel4.add(label1);
		
		tf2 = new JTextField(5);
		tf2.setForeground(new Color(255, 255, 240));
		tf2.setBackground(new Color(128, 0, 32));
		panel4.add(tf2);
		
		bt2 = new JButton(" Apply Discount ");
		bt2.setFont(new Font("Garamond", Font.BOLD, 14));
		bt2.setBackground(new Color(34, 139, 34));
		bt2.setForeground(new Color(255, 255, 255));
		bt2.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(c1, 3),
			BorderFactory.createEmptyBorder(1, 1, 1, 1)
		));
		bt2.addActionListener(this);
		panel4.add(bt2);
		panel3.add(panel4, BorderLayout.NORTH);
		
		// STEP: Order summary text area
		ta1 = new JTextArea();
		ta1.setEditable(false);
		ta1.setFont(new Font("Segoe UI", Font.BOLD, 16));
		ta1.setBackground(Color.PINK);
		ta1.setForeground(new Color(128, 0, 32));
		ta1.setText("Welcome to Hameem's Dhaba Sir! \nYour order:\n");
		JScrollPane scrollPane1 = new JScrollPane(ta1);
		scrollPane1.setPreferredSize(new Dimension(400, 500));
		panel3.add(scrollPane1, BorderLayout.CENTER);
		
		// STEP: Create customer panel (right side)
		panel5 = new JPanel(new GridBagLayout());
		panel5.setBackground(c3);
		panel5.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(c1, 3), 
			"Customer Information", 
			javax.swing.border.TitledBorder.TOP,
			javax.swing.border.TitledBorder.TOP, 
			f1, 
			c1
		));
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(6, 8, 6, 8);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy = 0;
		
		// SECTION: Name field
		label2 = new JLabel("Customer Name:");
		label2.setFont(f3);
		label2.setForeground(c5);
		gc.gridx = 0; gc.weightx = 0;
		panel5.add(label2, gc);
		
		tf1 = new JTextField();
		tf1.setFont(f4);
		tf1.setPreferredSize(new Dimension(200, 30));
		tf1.setBackground(Color.pink);
		tf1.setForeground(c5);
		tf1.setBorder(BorderFactory.createLineBorder(c1, 1));
		gc.gridx = 1; gc.weightx = 1;
		panel5.add(tf1, gc);
		
		// SECTION: Gender selection
		label3 = new JLabel("Customer Gender:");
		label3.setFont(f3);
		label3.setForeground(c5);
		gc.gridy++; gc.gridx = 0; gc.weightx = 0;
		panel5.add(label3, gc);
		
		JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		genderPanel.setBackground(c3);
		
		rb1 = new JRadioButton("Male");
		rb2 = new JRadioButton("Female");
		rb3 = new JRadioButton("Other");
		rb4 = new JRadioButton("Monkey");
		
		Font radioFont = new Font("Fraktur", Font.BOLD, 15);
		rb1.setFont(radioFont);
		rb2.setFont(radioFont);
		rb3.setFont(radioFont);
		rb4.setFont(radioFont);
		rb1.setForeground(c5);
		rb2.setForeground(c5);
		rb3.setForeground(c5);
		rb4.setForeground(c5);
		rb1.setBackground(c3);
		rb2.setBackground(c3);
		rb3.setBackground(c3);
		rb4.setBackground(c3);
		
		bg1 = new ButtonGroup();
		bg1.add(rb1);
		bg1.add(rb2);
		bg1.add(rb3);
		bg1.add(rb4);
		rb1.setSelected(true);
		
		genderPanel.add(rb1);
		genderPanel.add(rb2);
		genderPanel.add(rb3);
		genderPanel.add(rb4);
		gc.gridx = 1; gc.weightx = 1;
		panel5.add(genderPanel, gc);
		
		// SECTION: Age selection
		label4 = new JLabel("Customer Age:");
		label4.setFont(f3);
		label4.setForeground(c5);
		gc.gridy++; gc.gridx = 0; gc.weightx = 0;
		panel5.add(label4, gc);
		
		jb1 = new JComboBox<>();
		jb1.setFont(f4);
		jb1.setPreferredSize(new Dimension(100, 30));
		jb1.setBackground(new Color(255, 255, 240));
		jb1.setForeground(c5);
		for (int i = 15; i <= 109; i++) jb1.addItem(i);
		gc.gridx = 1; gc.weightx = 1;
		panel5.add(jb1, gc);
		
		// SECTION: Cash input & change (moved to top section)
		gc.gridy++; gc.gridx = 0; gc.gridwidth = 1; gc.fill = GridBagConstraints.HORIZONTAL;
		JLabel cashLbl = new JLabel("Cash Given:");
		cashLbl.setFont(f3);
		cashLbl.setForeground(c5);
		panel5.add(cashLbl, gc);
		gc.gridx = 1; gc.weightx = 1;
		tfCash = new JTextField();
		tfCash.setFont(f4);
		tfCash.setBackground(Color.pink);
		tfCash.setForeground(c5);
		tfCash.setBorder(BorderFactory.createLineBorder(c1, 1));
		panel5.add(tfCash, gc);
		
		// SECTION: Receipt area (fixed size)
		ta2 = new JTextArea();
		ta2.setEditable(false);
		ta2.setFont(new Font("Playfair Display", Font.BOLD, 15));
		ta2.setBackground(new Color(128, 0, 32));
		ta2.setForeground(new Color(255, 255, 240));
		if (ta2.getText().isEmpty()) {
			ta2.setText("Order invoice will appear here after confirming order.\nPlease order something first.\n");
		}
		gc.gridy++; gc.gridx = 0; gc.gridwidth = 10;
		gc.fill = GridBagConstraints.BOTH;
		JScrollPane receiptScroll = new JScrollPane(ta2);
		receiptScroll.setPreferredSize(new Dimension(380, 400));
		panel5.add(receiptScroll, gc);
		
		// STEP: Create action buttons (top)
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 4, 15, 15));
		actionPanel.setBackground(c4);
		actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		
		bt3 = new JButton("Remove Last Item");
		bt3.setFont(f2);
		bt3.setBackground(c1);
		bt3.setForeground(Color.WHITE);
		bt3.setBorder(BorderFactory.createLineBorder(c5, 1));
		bt3.setFocusPainted(false);
		bt3.setPreferredSize(new Dimension(150, 40));
		bt3.addActionListener(this);
		actionPanel.add(bt3);
		
		bt4 = new JButton("Clear Order");
		bt4.setFont(f2);
		bt4.setBackground(c1);
		bt4.setForeground(Color.WHITE);
		bt4.setBorder(BorderFactory.createLineBorder(c5, 1));
		bt4.setFocusPainted(false);
		bt4.setPreferredSize(new Dimension(150, 40));
		bt4.addActionListener(this);
		actionPanel.add(bt4);
		
		bt5 = new JButton("Confirm Order");
		bt5.setFont(f2);
		bt5.setBackground(c1);
		bt5.setForeground(Color.WHITE);
		bt5.setBorder(BorderFactory.createLineBorder(c5, 1));
		bt5.setFocusPainted(false);
		bt5.setPreferredSize(new Dimension(150, 40));
		bt5.addActionListener(this);
		actionPanel.add(bt5);
		
		bt1 = new JButton("Show Total");
		bt1.setFont(f2);
		bt1.setBackground(c1);
		bt1.setForeground(Color.WHITE);
		bt1.setBorder(BorderFactory.createLineBorder(c5, 1));
		bt1.setFocusPainted(false);
		bt1.setPreferredSize(new Dimension(150, 40));
		bt1.addActionListener(this);
		actionPanel.add(bt1);
		
		// STEP: Status bar
		label5 = new JLabel("Welcome!");
		label5.setFont(f4);
		label5.setForeground(Color.WHITE);
		label5.setBackground(c1);
		label5.setOpaque(true);
		label5.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		
		// STEP: Add logo
		img1 = loadIcon("logo.jpg", 100, 90);
		if (img1 != null) {
			label6 = new JLabel(img1);
			actionPanel.add(label6);
		}
		
		// STEP: Add top-level panels to frame
		panel1.add(panel2, BorderLayout.WEST);
		panel1.add(panel3, BorderLayout.CENTER);
		panel1.add(panel5, BorderLayout.EAST);
		panel1.add(actionPanel, BorderLayout.NORTH);
		panel1.add(label5, BorderLayout.SOUTH);
		super.add(panel1);
	}
	

	public void mouseClicked(MouseEvent me) 
	{	
		
	}
	public void mousePressed(MouseEvent me) 
	{ 
		
	}
	public void mouseReleased(MouseEvent me) 
	{ 
		
	}
	public void mouseEntered(MouseEvent me) 
	{ 
		
	}
	public void mouseExited(MouseEvent me) 
	{
		
	}
	
		public void actionPerformed(ActionEvent ae)
	{
		// STEP: Events handling
		if(ae.getSource() == bt2) // Apply Discount button
		{
			try {
				double discount = Double.parseDouble(tf2.getText());
				orderService.applyDiscount(currentOrder, discount);
				ta2.append("\nDiscount set: " + String.format("%.1f", discount) + "% (applies on confirm)\n");
				label5.setText("Discount noted; will apply on confirm");
				refreshSummary();
			} catch (Exception ex) {
				ta2.append("\nInvalid discount input!\n");
				label5.setText("Invalid discount input");
			}
		}
		if(ae.getSource() == bt3) // Remove Last Item button
		{
			try {
				if (orderService.removeItemFromOrder(currentOrder, "")) {
					label5.setText("Removed last item");
					refreshSummary();
				} else {
					label5.setText("No items to remove");
				}
			} catch (Exception e) {
				label5.setText("Error: " + e.getMessage());
			}
		}
		if(ae.getSource() == bt4) // Clear Order button
		{
			try {
				currentOrder.clear();
				label5.setText("Order cleared");
				refreshSummary();
			} catch (Exception e) {
				label5.setText("Error: " + e.getMessage());
			}
		}
		if(ae.getSource() == bt5) // Confirm Order button
		{
			try {
				// Validate customer information; if any empty, print notice in receipt and stop
				String name = tf1.getText().trim();
				Integer ageVal = (Integer) jb1.getSelectedItem();
				boolean genderSelected = rb1.isSelected() || rb2.isSelected() || rb3.isSelected() || rb4.isSelected();
				if (name.isEmpty() || ageVal == null || !genderSelected) {
					ta2.append("\nPlease fill all customer info before confirming the order.\n");
					label5.setText("Customer info incomplete");
					return;
				}
				
				if (currentOrder.isEmpty()) {
					label5.setText("Your order is empty");
					return;
				}
				
				// Get customer information (already validated)
				customer.setName(name);
				customer.setAge(ageVal);
				String gender = rb1.isSelected()?"Male": rb2.isSelected()?"Female": rb3.isSelected()?"Other":"Monkey";
				customer.setGender(gender);
				
				// Confirm order (locks manual discount)
				orderService.confirmOrder(currentOrder);
				
				// Append receipt without clearing old ones
				ta2.append("\n================ RECEIPT ================\n");
				ta2.append(currentOrder.getBill());
				
				// Calculate change if cash provided
				String cashText = tfCash != null ? tfCash.getText().trim() : "";
				if (!cashText.isEmpty()) {
					try {
						double cash = Double.parseDouble(cashText);
						double total = currentOrder.getTotalPrice();
						double change = cash - total;
						ta2.append("Cash Given: " + String.format("%.2f", cash) + "৳\n");
						ta2.append("Change: " + String.format("%.2f", change) + "৳\n");
					} catch (NumberFormatException nfe) {
						ta2.append("Cash input invalid.\n");
					}
				}
				
				label5.setText("Order confirmed!");
				
				// Save data (orders, customers, report)
				saveOrderData();
				
				
				tf1.setText("");
				bg1.clearSelection(); rb1.setSelected(true);
				jb1.setSelectedItem(15);
				tf2.setText("");
				tfCash.setText("");
				currentOrder = orderService.createOrder(customer);
				refreshSummary();
				
			} catch (Exception e) {
				label5.setText("Error: " + e.getMessage());
			}
		}
		else if(ae.getSource() == bt1) // Show Total button
		{
			try {
				refreshSummary();
				String totalMsg = "Current Total (no manual discount yet): " + String.format("%.2f", currentOrder.getTotalPrice()) + "৳";
				label5.setText(totalMsg);
				ta2.append(totalMsg + "\nItems: " + currentOrder.getTotalItems() + "\n");
			} catch (Exception e) {
				label5.setText("Error: " + e.getMessage());
			}
		}
		// Handle menu item buttons
		else if (ae.getSource() instanceof JButton) {
			JButton clickedButton = (JButton) ae.getSource();
			Entity.MenuItem item = (Entity.MenuItem) clickedButton.getClientProperty("menuItem");
			if (item != null) {
				try {
					orderService.addItemToOrder(currentOrder, item, 1);
					label5.setText("Added: " + item.getName());
					refreshSummary();
				} catch (Exception e) {
					label5.setText("Error: " + e.getMessage());
				}
			}
		}
	}
	

	
   
	
	private void refreshSummary() {
		ta1.setText("Welcome to Hameem's Dhaba Sir!\nYour order:\n");
		for (OrderItem item : currentOrder.getItems()) {
			Entity.MenuItem mi = item.getItem();
			String details = formatItemDetailsForSummary(mi);
			StringBuilder line = new StringBuilder();
			line.append(mi.getName());
			if (!details.isEmpty()) {
				line.append(" [").append(details).append("]");
			}
			line.append(" x").append(item.getQuantity())
			    .append(String.format(" %.2f", item.getTotalPrice()))
			    .append("৳");
			ta1.append(line.toString());
			ta1.append("\n");
		}
		ta1.append("=====================\nTotal: " + 
			  String.format("%.2f", currentOrder.getTotalPrice()) + "৳\n");
	}
	
	// Helper to show item attributes in the summary
	private String formatItemDetailsForSummary(Entity.MenuItem item) {
		try {
			if (item instanceof Entity.Food) {
				Entity.Food f = (Entity.Food) item;
				String veg = f.isVegetarian() ? "Veg" : "Non-Veg";
				return f.getCuisine() + ", " + f.getSpiceLevel() + ", " + veg;
			} else if (item instanceof Entity.Drinks) {
				Entity.Drinks d = (Entity.Drinks) item;
				return d.getServingInfo();
			} else {
				return item.getCategory();
			}
		} catch (Exception e) {
			return item.getCategory();
		}
	}
	
	private void saveOrderData() {
		try {
			// Get all orders
			List<Order> allOrders = ((OrderServiceImpl) orderService).getAllOrders();
			
			// Create customers list
			List<Customer> customers = new ArrayList<>();
			customers.add(customer);
			
			// Save data
			DataManager.saveOrders(allOrders);
			DataManager.saveCustomers(customers);
			DataManager.generateReport(allOrders, customers);
			
			label5.setText("Data saved successfully");
		} catch (Exception e) {
			label5.setText("Error saving data: " + e.getMessage());
		}
	}
	
	private ImageIcon loadIcon(String filename, int w, int h) {
		String[] candidates = {"/images/" + filename, "/cafe/images/" + filename};
		java.net.URL url = null;
		for (String path : candidates) {
			url = getClass().getResource(path);
			if (url != null) break;
		}
		if (url == null) {
			java.io.File fsFile = new java.io.File("src/cafe/images", filename);
			if (fsFile.exists()) {
				ImageIcon fsIcon = new ImageIcon(fsFile.getAbsolutePath());
				Image scaledFs = fsIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
				return new ImageIcon(scaledFs);
			}
			return null;
		}
		ImageIcon icon = new ImageIcon(url);
		Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}
}

