/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Мариям
 */
// March 5, 2004 (Previously Feb 27) - 6_David_Sharp_Chris

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

// The class draws the rectangles, the border if required, and
// fills in the rectangles with the specified colors.
public class RectanglesPanel extends JPanel
{
	private int myRows,myCols,myOldRows,myOldCols,myMouseX,myMouseY;
	private Color myColor;
	private Color [][] myColors;
	private boolean myGrid;
	private boolean myMouseValid = false;

	// Initialize with mouse invalid and not clicked.
	public RectanglesPanel(int rows, int cols)
	{
		myColors = new Color[rows][cols];
		for (int i=0; i<rows; i++)
			for (int j=0; j<cols; j++)
				myColors[i][j] = Color.WHITE;
		myRows = myOldRows = rows;
		myCols = myOldCols = cols;
		myMouseValid = false;
	}

	// Adjust array size
	private void adjustArray(int oldrows, int rows, int oldcols, int cols)
	{
		Color[][] temp = new Color[rows][cols];

		for (int i=0; i<rows; i++)
			for (int j=0; j<cols; j++)
				if (i < oldrows && j < oldcols)
					temp[i][j] = myColors[i][j];
		myRows = myOldRows = rows;
		myCols = myOldCols = cols;
		myColors = temp;
	}

	// Update state of draw/not draw grid.
	public void drawRectangles(boolean grid)
	{
		myGrid = grid;
		repaint();
	}

	// Update clicked mouse coordinates.
	public void setMouseXY(int x, int y)
	{
		myMouseX = x;
		myMouseY = y;
		repaint();
	}

	// Update rows.
	public void changeRows(int rows)
	{
		myRows = rows;
		repaint();
	}

	// Update columns.
	public void changeCols(int cols)
	{
		myCols = cols;
		repaint();
	}

	// Update color.
	public void setColor(Color color)
	{
		myColor = color;
	}

	// Paint screen.
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		int width   = this.getWidth();
		int height  = this.getHeight();
		int rowPixels = width/myCols;
		int colPixels = height/myRows;
		double leftBorder,rightBorder,topBorder,bottomBorder;

		// Adjust array size if required.
		if (myRows != myOldRows || myCols != myOldCols)
			adjustArray(myOldRows, myRows, myOldCols, myCols);

		// Calculate borders, if present.
		int i = width%myCols;
		if (i > 0) // Draw left and right borders.
		{
			if (i%2 == 0) // Even pixels.
			{
				leftBorder = i/2 - 1;
				rightBorder = width - i/2;
			}
			else // Odd pixels.
			{
				leftBorder = (i+1)/2 - 1;
				rightBorder = width - (i-1)/2;
			}
		}
		else // Set borders off screen, i.e. none.
		{
			leftBorder = -1.0;
			rightBorder = width + 1;
		}
		i = height%myRows;
		if (i > 0) // Draw top and bottom borders.
		{
			if (i%2 == 0) // Even pixels.
			{
				topBorder = i/2 - 1;
				bottomBorder = height - i/2;
			}
			else // Odd pixels.
			{
				topBorder = (i+1)/2 - 1;
				bottomBorder = height - (i-1)/2;
			}
		}
		else // Set borders off screen, i.e. none.
		{
			topBorder = -1;
			bottomBorder = height + 1;
		}

		g2.setPaint(Color.BLACK);

		// Draw borders if required.
		Line2D.Double line = new Line2D.Double();
		if (leftBorder >= 0)
		{
			for (i=0; i<=leftBorder; i++)
			{
				line.setLine(i, 0, i, height);
				g2.draw(line);
			}
		}
		if (rightBorder < width)
		{
			for (i=(int)rightBorder; i<width; i++)
			{
				line.setLine(i, 0, i, height);
				g2.draw(line);
			}
		}
		if (topBorder >= 0)
		{
			for (i=0; i<=topBorder; i++)
			{
				line.setLine(0, i, width,i);
				g2.draw(line);
			}
		}
		if (bottomBorder < height)
		{
			for (i=(int)bottomBorder; i<height; i++)
			{
				line.setLine(0, i, width,i);
				g2.draw(line);
			}
		}

		// Test to see if last mouse click is in range.
		if (myMouseX > leftBorder && myMouseX < rightBorder &&
			myMouseY > topBorder &&  myMouseY < bottomBorder)
			myMouseValid = true;

		// Draw colred rectangles, updated with last mouse click if valid.
		Rectangle2D.Double box = new Rectangle2D.Double();
		for (i=0; i<myRows; i++)
		{
			int top = i*colPixels+(int)topBorder+1;
			for (int j=0; j<myCols; j++)
			{
				int left = j*rowPixels+(int)leftBorder+1;
				box.setRect(left, top, rowPixels, colPixels);
				if (myColors[i][j] != null)
				{
					g2.setPaint(myColors[i][j]);
					g2.fill(box);
				}
				// If mouse is valid, find mouse's row and column.
				if (myMouseValid && left + rowPixels - 1 >= myMouseX &&
					top + colPixels - 1 >= myMouseY)
				{
					// Trap invalid mouse click on grid.
					if (myGrid && (left + rowPixels - 1 == myMouseX ||
						top + colPixels - 1 == myMouseY))
						myMouseValid = false;
					myMouseX = -1;
					myMouseY = -1;
					// Fill in rectangle with selected color, then set
					// mouse to invalid.
					if (myMouseValid)
					{
						myColors[i][j] = myColor;
						g2.setPaint(myColor);
						g2.fill(box);
						myMouseValid = false;
					}
				}
			}
		}

		// Draw grid lines if selected.
		if (myGrid)
		{
			g2.setPaint(Color.BLACK);
			double coord;
			for (i = 0; i<myCols; i++) // Draw verticles lines.
			{
				coord = (double)((i+1)*rowPixels)+leftBorder;
				line.setLine(coord, 0.0, coord, height);
				g2.draw(line);
			}

			for (i = 0; i<myRows; i++) // Draw horizontal lines
			{
				coord = (double)((i+1)*colPixels)+topBorder;
				line.setLine(0.0, coord, width, coord);
				g2.draw(line);
			}
		}
	}
}