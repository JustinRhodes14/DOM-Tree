# Dom-Tree
This project reads HTML files, and will return the DOM Tree for the given file, it allows for users to replace tags, add tags, delete tags, and of course can print the whole tree.

Utilizing Trees, this program reads in HTML files, storing them relevant to where they appear in the HTML file. For example, the firstchild of table elements can also be another table since they appear on the same block. The sibling is what appears within a given tag, for example, text, or a <b> tag could appear their. Much of this program was made recursively, which made it much easier to iterate through each respective tree.
