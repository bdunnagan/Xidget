Layout Algorithm

The anchor layout algorithm uses attachment declarations to calculate the position of the 
four sides of a widget. The algorithm caches a dependency sorted list of computation nodes
for extremely efficient layout.

The coordinate of the side of a widget is undefined until it is connected to a defined 
coordinate of another widget. The top-level container has x0 and y0 defined by default.
Therefore, most widgets will have correctly defined x0 and y0 coordinates if the are
appropriately attached.  Widgets which use proportional attachment cannot be computed
unless their container has values for x1 and y1.

There are two strategies for defining attachments. One is appropriate for containers
which have a predefined size, while the other is appropriate for containers which 
should be sized to fit their contents (like dialogs).

In the first case, the implementation must insure that containers with predefined
sizes specify those sizes to their WidgetRightNode and WidgetBottomNode anchors.

In the second case, it is the responsibility of the layout declaration to anchor
the right and bottom sides of the container to the appropriate child.
