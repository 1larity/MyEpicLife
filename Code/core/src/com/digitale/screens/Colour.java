package com.digitale.screens;

import com.badlogic.gdx.graphics.Color;

public class Colour extends Color {
	public static final Color CLEAR = new Color(0, 0, 0, 0);
	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color LIGHT_GRAY = new Color(0.75f, 0.75f, 0.75f, 1);
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1);
	public static final Color DARK_GRAY = new Color(0.25f, 0.25f, 0.25f, 1);
	public static final Color PINK = new Color(1, 0.68f, 0.68f, 1);
	public static final Color ORANGE = new Color(1, 0.78f, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color MAGENTA = new Color(1, 0, 1, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color OLIVE = new Color(0.5f, 0.5f, 0, 1);
	public static final Color PURPLE = new Color(0.5f, 0, 0.5f, 1);
	public static final Color MAROON = new Color(0.5f, 0, 0, 1);
	public static final Color TEAL = new Color(0, 0.5f, 0.5f, 1);
	public static final Color NAVY = new Color(0, 0, 0.5f, 1);
	}
	/*public static final Color white">#FFFFFF</color>
	public static final Color ivory">#FFFFF0</color>
	public static final Color lightyellow">#FFFFE0</color>
	public static final Color yellow">#FFFF00</color>
	public static final Color snow">#FFFAFA</color>
	public static final Color floralwhite">#FFFAF0</color>
	public static final Color lemonchiffon">#FFFACD</color>
	public static final Color cornsilk">#FFF8DC</color>
	public static final Color seashell">#FFF5EE</color>
	public static final Color lavenderblush">#FFF0F5</color>
	public static final Color papayawhip">#FFEFD5</color>
	    <color name="blanchedalmond">#FFEBCD</color>
	    <color name="mistyrose">#FFE4E1</color>
	    <color name="bisque">#FFE4C4</color>
	    <color name="moccasin">#FFE4B5</color>
	    <color name="navajowhite">#FFDEAD</color>
	    <color name="peachpuff">#FFDAB9</color>
	    <color name="gold">#FFD700</color>
	    <color name="pink">#FFC0CB</color>
	    <color name="lightpink">#FFB6C1</color>
	    <color name="orange">#FFA500</color>
	    <color name="lightsalmon">#FFA07A</color>
	    <color name="darkorange">#FF8C00</color>
	    <color name="coral">#FF7F50</color>
	    <color name="hotpink">#FF69B4</color>
	    <color name="tomato">#FF6347</color>
	    <color name="orangered">#FF4500</color>
	    <color name="deeppink">#FF1493</color>
	    <color name="fuchsia">#FF00FF</color>
	    <color name="magenta">#FF00FF</color>
	    <color name="red">#FF0000</color>
	    <color name="oldlace">#FDF5E6</color>
	    <color name="lightgoldenrodyellow">#FAFAD2</color>
	    <color name="linen">#FAF0E6</color>
	    <color name="antiquewhite">#FAEBD7</color>
	    <color name="salmon">#FA8072</color>
	    <color name="ghostwhite">#F8F8FF</color>
	    <color name="mintcream">#F5FFFA</color>
	    <color name="whitesmoke">#F5F5F5</color>
	    <color name="beige">#F5F5DC</color>
	    <color name="wheat">#F5DEB3</color>
	    <color name="sandybrown">#F4A460</color>
	    <color name="azure">#F0FFFF</color>
	    <color name="honeydew">#F0FFF0</color>
	    <color name="aliceblue">#F0F8FF</color>
	    <color name="khaki">#F0E68C</color>
	    <color name="lightcoral">#F08080</color>
	    <color name="palegoldenrod">#EEE8AA</color>
	    <color name="violet">#EE82EE</color>
	    <color name="darksalmon">#E9967A</color>
	    <color name="lavender">#E6E6FA</color>
	    <color name="lightcyan">#E0FFFF</color>
	    <color name="burlywood">#DEB887</color>
	    <color name="plum">#DDA0DD</color>
	    <color name="gainsboro">#DCDCDC</color>
	    <color name="crimson">#DC143C</color>
	    <color name="palevioletred">#DB7093</color>
	    <color name="goldenrod">#DAA520</color>
	    <color name="orchid">#DA70D6</color>
	    <color name="thistle">#D8BFD8</color>
	    <color name="lightgrey">#D3D3D3</color>
	    <color name="tan">#D2B48C</color>
	    <color name="chocolate">#D2691E</color>
	    <color name="peru">#CD853F</color>
	    <color name="indianred">#CD5C5C</color>
	    <color name="mediumvioletred">#C71585</color>
	    <color name="silver">#C0C0C0</color>
	    <color name="darkkhaki">#BDB76B</color>
	    <color name="rosybrown">#BC8F8F</color>
	    <color name="mediumorchid">#BA55D3</color>
	    <color name="darkgoldenrod">#B8860B</color>
	    <color name="firebrick">#B22222</color>
	    <color name="powderblue">#B0E0E6</color>
	    <color name="lightsteelblue">#B0C4DE</color>
	    <color name="paleturquoise">#AFEEEE</color>
	    <color name="greenyellow">#ADFF2F</color>
	    <color name="lightblue">#ADD8E6</color>
	    <color name="darkgray">#A9A9A9</color>
	    <color name="brown">#A52A2A</color>
	    <color name="sienna">#A0522D</color>
	    <color name="yellowgreen">#9ACD32</color>
	    <color name="darkorchid">#9932CC</color>
	    <color name="palegreen">#98FB98</color>
	    <color name="darkviolet">#9400D3</color>
	    <color name="mediumpurple">#9370DB</color>
	    <color name="lightgreen">#90EE90</color>
	    <color name="darkseagreen">#8FBC8F</color>
	    <color name="saddlebrown">#8B4513</color>
	    <color name="darkmagenta">#8B008B</color>
	    <color name="darkred">#8B0000</color>
	    <color name="blueviolet">#8A2BE2</color>
	    <color name="lightskyblue">#87CEFA</color>
	    <color name="skyblue">#87CEEB</color>
	    <color name="gray">#808080</color>
	    <color name="olive">#808000</color>
	    <color name="purple">#800080</color>
	    <color name="maroon">#800000</color>
	    <color name="aquamarine">#7FFFD4</color>
	    <color name="chartreuse">#7FFF00</color>
	    <color name="lawngreen">#7CFC00</color>
	    <color name="mediumslateblue">#7B68EE</color>
	    <color name="lightslategray">#778899</color>
	    <color name="slategray">#708090</color>
	    <color name="olivedrab">#6B8E23</color>
	    <color name="slateblue">#6A5ACD</color>
	    <color name="dimgray">#696969</color>
	    <color name="mediumaquamarine">#66CDAA</color>
	    <color name="cornflowerblue">#6495ED</color>
	    <color name="cadetblue">#5F9EA0</color>
	    <color name="darkolivegreen">#556B2F</color>
	    <color name="indigo">#4B0082</color>
	    <color name="mediumturquoise">#48D1CC</color>
	    <color name="darkslateblue">#483D8B</color>
	    <color name="steelblue">#4682B4</color>
	    <color name="royalblue">#4169E1</color>
	    <color name="turquoise">#40E0D0</color>
	    <color name="mediumseagreen">#3CB371</color>
	    <color name="limegreen">#32CD32</color>
	    <color name="darkslategray">#2F4F4F</color>
	    <color name="seagreen">#2E8B57</color>
	    <color name="forestgreen">#228B22</color>
	    <color name="lightseagreen">#20B2AA</color>
	    <color name="dodgerblue">#1E90FF</color>
	    <color name="midnightblue">#191970</color>
	    <color name="aqua">#00FFFF</color>
	    <color name="cyan">#00FFFF</color>
	    <color name="springgreen">#00FF7F</color>
	    <color name="lime">#00FF00</color>
	    <color name="mediumspringgreen">#00FA9A</color>
	    <color name="darkturquoise">#00CED1</color>
	    <color name="deepskyblue">#00BFFF</color>
	    <color name="darkcyan">#008B8B</color>
	    <color name="teal">#008080</color>
	    <color name="green">#008000</color>
	    <color name="darkgreen">#006400</color>
	    <color name="blue">#0000FF</color>
	    <color name="mediumblue">#0000CD</color>
	    <color name="darkblue">#00008B</color>
	    <color name="navy">#000080</color>
	    <color name="black">#000000</color>
}
*/