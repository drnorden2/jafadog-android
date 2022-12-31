package com.jafadog;


import java.util.Hashtable;


public abstract class Graphics implements ProprietaryDrawable {
	public static final int OFFSET = 32000;
	protected Hashtable binding;	
	protected int scaleX = 8;
	protected int scaleY = 8;
	protected Hashtable<String,Object> bitmaps = new Hashtable<String,Object>();

	
	abstract public char[][] bindGraphicsBanner(String graphicName, int beginAscii, int xSquares, int ySquares);

	abstract public boolean bindGraphics(char c, Object o);

	abstract public Object getImage(String path);
	
	abstract public Object getUncachedCharAsImage(char c, int bg, int fg);
	

	
	public void create( int scaleX, int scaleY) {
		System.out.println("Create Graphics");
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		binding = new Hashtable();
	}
	
    
   

	public boolean bindGraphics(char c, String graphicName) {
		System.out.println(graphicName);
		return bindGraphics(c, getImage(graphicName));
	}

	
	public final  Object  getCharAsImage(char c, int bg, int fg) {
		String key = "" + c + "|" + fg + "|" + bg;
		Object bm = (Object) bitmaps.get(key);
		if (bm == null) {
			bm = getUncachedCharAsImage(c,bg,fg);
		
			if (bm != null) {
                bitmaps.put(key, bm);
            }
		}
		return bm;
	}
	
	public Object getGraphics(char c) {
		try {
			return  binding.get("" + c);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public boolean isBound(char c) {
		return (this.binding.get("" + c) != null);
	}
	
	
	
	private int charToNibble(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 0xa;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 0xa;
		} else {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
	}
	
	
	/*used in super class*/
	public int[] getCharToArray(char c, int fgCode, int bgCode) {
		String s;
		if (c==1) {
			 s ="";
		}
		if (c < 1 || c > 255)
			s = "00000000ffffffff";
		else
			s = data[c - 1];
		int stringLength = s.length();
		int[] array = new int[8 * 8];
		int index = 0;
		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			int all = (int) ((high << 4) | low);
			for (int k = 0; k < 8; k++) {
				index = (i / 2) * 8 + k;
				int pot = 1; // shift in other dir from 2 to 1 with << not possible
				if (k < 7)
					pot = 2 << (6 - k);
				if (all - pot >= 0) {
					all -= pot;
					array[index] = fgCode;

				} else {
					array[index] = bgCode;

				}
			}
		}
		return array;
	}
	

	public final void update() {
		//@TODO
		//ui.update();
	}

	
	 private final String[] data = { "ffffffffffffffff"/*"7e81a581bd99817e"*/, "7effdbffc3e7ff7e", "6cfefefe7c381000", "10387cfe7c381000",
				"387c38fefed61038", "10387cfefe7c1038", "0000000000000000", "ffffe7c3c3e7ffff", "0000000000000000",
				"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000", "7f637f636367e6c0",
				"18db3ce7e73cdb18", "80e0f8fef8e08000", "020e3efe3e0e0200", "183c7e18187e3c18", "6666666666006600",
				"7fdbdb7b1b1b1b00", "3e613c66663c867c", "000000007e7e7e00", "183c7e187e3c18ff", "183c7e1818181800",
				"181818187e3c1800", "00180cfe0c180000", "003060fe60300000", "0000000000000000", "0000000000000000",
				"0000000000000000", "0000000000000000", "0000000000000000", "183c3c1818001800", "6666240000000000",
				"6c6cfe6cfe6c6c00", "183e603c067c1800", "00c6cc183066c600", "386c3876dccc7600", "1818300000000000",
				"0c18303030180c00", "30180c0c0c183000", "00663cff3c660000", "0018187e18180000", "0000000000181830",
				"0000007e00000000", "0000000000181800", "060c183060c08000", "386cc6d6c66c3800", "1838181818187e00",
				"7cc6061c3066fe00", "7cc6063c06c67c00", "1c3c6cccfe0c1e00", "fec0c0fc06c67c00", "3860c0fcc6c67c00",
				"fec60c1830303000", "7cc6c67cc6c67c00", "7cc6c67e060c7800", "0018180000181800", "0018180000181830",
				"060c1830180c0600", "00007e00007e0000", "6030180c18306000", "7cc60c1818001800", "7cc6dededec07800",
				"386cc6fec6c6c600", "fc66667c6666fc00", "3c66c0c0c0663c00", "f86c6666666cf800", "fe6268786862fe00",
				"fe6268786860f000", "3c66c0c0ce663a00", "c6c6c6fec6c6c600", "3c18181818183c00", "1e0c0c0ccccc7800",
				"e6666c786c66e600", "f06060606266fe00", "c6eefefed6c6c600", "c6e6f6decec6c600", "7cc6c6c6c6c67c00",
				"fc66667c6060f000", "7cc6c6c6c6ce7c0e", "fc66667c6c66e600", "3c6630180c663c00", "7e7e5a1818183c00",
				"c6c6c6c6c6c67c00", "c6c6c6c6c66c3800", "c6c6c6d6d6fe6c00", "c6c66c386cc6c600", "6666663c18183c00",
				"fec68c183266fe00", "3c30303030303c00", "c06030180c060200", "3c0c0c0c0c0c3c00", "10386cc600000000",
				"00000000000000ff", "30180c0000000000", "0000780c7ccc7600", "e0607c666666dc00", "00007cc6c0c67c00",
				"1c0c7ccccccc7600", "00007cc6fec07c00", "3c6660f86060f000", "000076cccc7c0cf8", "e0606c766666e600",
				"1800381818183c00", "060006060666663c", "e060666c786ce600", "3818181818183c00", "0000ecfed6d6d600",
				"0000dc6666666600", "00007cc6c6c67c00", "0000dc66667c60f0", "000076cccc7c0c1e", "0000dc766060f000",
				"00007ec07c06fc00", "3030fc3030361c00", "0000cccccccc7600", "0000c6c6c66c3800", "0000c6d6d6fe6c00",
				"0000c66c386cc600", "0000c6c6c67e06fc", "00007e4c18327e00", "0e18187018180e00", "1818181818181800",
				"7018180e18187000", "76dc000000000000", "0010386cc6c6fe00", "7cc6c0c0c67c0c78", "cc00cccccccc7600",
				"0c187cc6fec07c00", "7c82780c7ccc7600", "c600780c7ccc7600", "3018780c7ccc7600", "3030780c7ccc7600",
				"00007ec0c07e0c38", "7c827cc6fec07c00", "c6007cc6fec07c00", "30187cc6fec07c00", "6600381818183c00",
				"7c82381818183c00", "3018003818183c00", "c6386cc6fec6c600", "386c7cc6fec6c600", "1830fec0f8c0fe00",
				"00007e12fe90fe00", "3e6cccfeccccce00", "7c827cc6c6c67c00", "c6007cc6c6c67c00", "30187cc6c6c67c00",
				"788400cccccc7600", "6030cccccccc7600", "c600c6c6c67e06fc", "c6386cc6c66c3800", "c600c6c6c6c67c00",
				"00027cced6e67c80", "386c64f06066fc00", "3a6cced6e66cb800", "00c66c386cc60000", "0e1b183c18d87000",
				"1830780c7ccc7600", "0c18003818183c00", "0c187cc6c6c67c00", "1830cccccccc7600", "76dc00dc66666600",
				"76dc00e6f6dece00", "3c6c6c3e007e0000", "386c6c38007c0000", "1800181830633e00", "7e81b9a5b9a5817e",
				"000000fe06060000", "63e66c7e3366cc0f", "63e66c7a366adf06", "180018183c3c1800", "003366cc66330000",
				"00cc663366cc0000", "2288228822882288", "55aa55aa55aa55aa", "77dd77dd77dd77dd", "1818181818181818",
				"18181818f8181818", "3060386cc6fec600", "7c82386cc6fec600", "180c386cc6fec600", "7e819da1a19d817e",
				"3636f606f6363636", "3636363636363636", "0000fe06f6363636", "3636f606fe000000", "18187ec0c07e1818",
				"66663c7e187e1818", "00000000f8181818", "181818181f000000", "18181818ff000000", "00000000ff181818",
				"181818181f181818", "00000000ff000000", "18181818ff181818", "76dc7c067ec67e00", "76dc386cc6fec600",
				"363637303f000000", "00003f3037363636", "3636f700ff000000", "0000ff00f7363636", "3636373037363636",
				"0000ff00ff000000", "3636f700f7363636", "00c67cc6c67cc600", "307e0c7ccccc7800", "f86c66f6666cf800",
				"7c82fec0fcc0fe00", "c600fec0fcc0fe00", "3018fec0fcc0fe00", "0000381818183c00", "0c183c1818183c00",
				"3c423c1818183c00", "66003c1818183c00", "18181818f8000000", "000000001f181818", "ffffffffffffffff",
				"00000000ffffffff", "1818180000181818", "30183c1818183c00", "ffffffff00000000", "3060386cc66c3800",
				"78ccccd8ccc6cc00", "7c82386cc66c3800", "0c06386cc66c3800", "76dc7cc6c6c67c00", "76dc386cc66c3800",
				"0000666666667cc0", "e0607c66667c60f0", "f0607c667c60f000", "1830c6c6c6c67c00", "7c8200c6c6c67c00",
				"6030c6c6c6c67c00", "1830c6c6c67e06fc", "0c1866663c183c00", "ff00000000000000", "0c18300000000000",
				"0000007e00000000", "18187e1818007e00", "0000000000ff00ff", "e132e43af62a5f86", "7fdbdb7b1b1b1b00",
				"3e613c66663c867c", "0018007e00180000", "0000000000180c38", "386c6c3800000000", "00c6000000000000",
				"0000001800000000", "183818183c000000", "780c380c78000000", "780c18307c000000", "00003c3c3c3c0000",
				"0000000000000000"

		};



}