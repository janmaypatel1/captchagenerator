package Imagecaptcha;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ImageIcon;

public class ImageCaptchaApplet extends JApplet{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Map<String, ImageIcon> images = new HashMap<>();
	    private final ArrayList<JLabel> imageLabels = new ArrayList<>();
	    private final JLabel promptLabel = new JLabel("Select all images with: ");
	    private final JButton verifyButton = new JButton("Verify");
	    private final JPanel gridPanel = new JPanel(new GridLayout(3 , 3));
	    private final Set<String> requiredCategory = new HashSet<>();
	    private final Set<JLabel> selectedLabels = new HashSet<>();
	    private String targetCategory;

	    @Override
	    public void init() {
	        try {
	            SwingUtilities.invokeAndWait(this::createGUI);
	        } catch (Exception e) {
	            System.err.println("Initialization failed.");
	        }
	    }

	    private void createGUI() {
	        loadImages();
	        setLayout(new BorderLayout());
	        add(promptLabel, BorderLayout.NORTH);
	        add(gridPanel, BorderLayout.CENTER);
	        add(verifyButton, BorderLayout.SOUTH);

	        verifyButton.addActionListener(e -> checkSelections());
	        populateGrid();
	    }

    private void loadImages() {
        // Load images into the map; these paths are just examples
        images.put("car", new ImageIcon("images/car_1.jpg"));
        images.put("bike", new ImageIcon("images/bike_1.jpg"));
        images.put("cat", new ImageIcon("images/cat_1.jpg"));
        images.put("dog", new ImageIcon("images/dog_1.jpg"));
        images.put("trafficsignal", new ImageIcon("images/trafficsignal_1.jpg"));
    }

    private void populateGrid() {
        List<String> keys = new ArrayList<>(images.keySet());
        
        Random random = new Random();
        gridPanel.removeAll();
        imageLabels.clear();

        for (int i = 0; i < 9; i++) {
            String category = keys.get(random.nextInt(keys.size()));
            JLabel label = new JLabel(images.get(category));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedLabels.contains(label)) {
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        selectedLabels.remove(label);
                    } else {
                        label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                        selectedLabels.add(label);
                    }
                }
            });
            gridPanel.add(label);
            imageLabels.add(label);
        }

        // Randomly select a category for the user to find
        targetCategory = keys.get(random.nextInt(keys.size()));
        promptLabel.setText("Select all images with: " + targetCategory);
        requiredCategory.clear();
        requiredCategory.add(targetCategory);
        validate();
    }

    
    	private void checkSelections() {
    		 boolean correct = true; // Initialize as true

    		    for (JLabel label : imageLabels) {
    		        ImageIcon icon = (ImageIcon) label.getIcon();

    		        // Log image description and selection status
    		        System.out.println("Image: " + icon.getDescription() + ", Selected: " + selectedLabels.contains(label));

    		        // Check if the label's icon description matches the target category,
    		        // and if the label is not in the selectedLabels set
    		        if (requiredCategory.contains(icon.getDescription()) && !selectedLabels.contains(label)) {
    		            correct = false;
    		            break; // No need to continue checking if one image is already incorrect
    		        }

    		        // Check if the label's icon description doesn't match the target category,
    		        // and if the label is in the selectedLabels set
    		        if (!requiredCategory.contains(icon.getDescription()) && selectedLabels.contains(label)) {
    		            correct = false;
    		            break; // No need to continue checking if one image is already incorrect
    		        }
    		    }

    		    if (correct) {
    		        JOptionPane.showMessageDialog(this, "Verification successful");
    		    } else {
    		        JOptionPane.showMessageDialog(this, "Verification failed!");
    		    }

    		    populateGrid(); // Refresh grid for new verification
    		}
}