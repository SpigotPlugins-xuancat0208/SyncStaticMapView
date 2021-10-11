package xuan.cat.syncstaticmapview.code.branch.v15_R1;

import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Branch_15_R1_MapColor implements BranchMapColor {
    private final byte[] colorIndexRGB = new byte[262144];
    private final List<Color> colorRegistry      = new ArrayList<>();
    private final int registryColorAmount;

    public Branch_15_R1_MapColor() {
        colorRegistry.add(new Color(0,   0,   0));    //   0
        colorRegistry.add(new Color(0,   0,   0));    //   1
        colorRegistry.add(new Color(0,   0,   0));    //   2
        colorRegistry.add(new Color(0,   0,   0));    //   3

        colorRegistry.add(new Color(89,  125, 39));   //   4
        colorRegistry.add(new Color(109, 153, 48));   //   5
        colorRegistry.add(new Color(127, 178, 56));   //   6
        colorRegistry.add(new Color(67,  94,  29));   //   7

        colorRegistry.add(new Color(174, 164, 115));  //   8
        colorRegistry.add(new Color(213, 201, 140));  //   9
        colorRegistry.add(new Color(247, 233, 163));  //  10
        colorRegistry.add(new Color(130, 123, 86));   //  11

        colorRegistry.add(new Color(140, 140, 140));  //  12
        colorRegistry.add(new Color(171, 171, 171));  //  13
        colorRegistry.add(new Color(199, 199, 199));  //  14
        colorRegistry.add(new Color(105, 105, 105));  //  15

        colorRegistry.add(new Color(180, 0,   0));    //  16
        colorRegistry.add(new Color(220, 0,   0));    //  17
        colorRegistry.add(new Color(255, 0,   0));    //  18
        colorRegistry.add(new Color(135, 0,   0));    //  19

        colorRegistry.add(new Color(112, 112, 180));  //  20
        colorRegistry.add(new Color(138, 138, 220));  //  21
        colorRegistry.add(new Color(160, 160, 255));  //  22
        colorRegistry.add(new Color(84,  84,  135));  //  23

        colorRegistry.add(new Color(117, 117, 117));  //  24
        colorRegistry.add(new Color(144, 144, 144));  //  25
        colorRegistry.add(new Color(167, 167, 167));  //  26
        colorRegistry.add(new Color(88,  88,  88));   //  27

        colorRegistry.add(new Color(0,   87,  0));    //  28
        colorRegistry.add(new Color(0,   106, 0));    //  29
        colorRegistry.add(new Color(0,   124, 0));    //  30
        colorRegistry.add(new Color(0,   65,  0));    //  31

        colorRegistry.add(new Color(180, 180, 180));  //  32
        colorRegistry.add(new Color(220, 220, 220));  //  33
        colorRegistry.add(new Color(255, 255, 255));  //  34
        colorRegistry.add(new Color(135, 135, 135));  //  35

        colorRegistry.add(new Color(115, 118, 129));  //  36
        colorRegistry.add(new Color(141, 144, 158));  //  37
        colorRegistry.add(new Color(164, 168, 184));  //  38
        colorRegistry.add(new Color(86,  88,  97));   //  39

        colorRegistry.add(new Color(106, 76,  54));   //  40
        colorRegistry.add(new Color(130, 94,  66));   //  41
        colorRegistry.add(new Color(151, 109, 77));   //  42
        colorRegistry.add(new Color(79,  57,  40));   //  43

        colorRegistry.add(new Color(79,  79,  79));   //  44
        colorRegistry.add(new Color(96,  96,  96));   //  45
        colorRegistry.add(new Color(112, 112, 112));  //  46
        colorRegistry.add(new Color(59,  59,  59));   //  47

        colorRegistry.add(new Color(45,  45,  180));  //  48
        colorRegistry.add(new Color(55,  55,  220));  //  49
        colorRegistry.add(new Color(64,  64,  255));  //  50
        colorRegistry.add(new Color(33,  33,  135));  //  51

        colorRegistry.add(new Color(100, 84,  50));   //  52
        colorRegistry.add(new Color(123, 102, 62));   //  53
        colorRegistry.add(new Color(143, 119, 72));   //  54
        colorRegistry.add(new Color(75,  63,  38));   //  55

        colorRegistry.add(new Color(180, 177, 172));  //  56
        colorRegistry.add(new Color(220, 217, 211));  //  57
        colorRegistry.add(new Color(255, 252, 245));  //  58
        colorRegistry.add(new Color(135, 133, 129));  //  59

        colorRegistry.add(new Color(152, 89,  36));   //  60
        colorRegistry.add(new Color(186, 109, 44));   //  61
        colorRegistry.add(new Color(216, 127, 51));   //  62
        colorRegistry.add(new Color(114, 67,  27));   //  63

        colorRegistry.add(new Color(125, 53,  152));  //  64
        colorRegistry.add(new Color(153, 65,  186));  //  65
        colorRegistry.add(new Color(178, 76,  216));  //  66
        colorRegistry.add(new Color(94,  40,  114));  //  67

        colorRegistry.add(new Color(72,  108, 152));  //  68
        colorRegistry.add(new Color(88,  132, 186));  //  69
        colorRegistry.add(new Color(102, 153, 216));  //  70
        colorRegistry.add(new Color(54,  81,  114));  //  71

        colorRegistry.add(new Color(161, 161, 36));   //  72
        colorRegistry.add(new Color(197, 197, 44));   //  73
        colorRegistry.add(new Color(229, 229, 51));   //  74
        colorRegistry.add(new Color(121, 121, 27));   //  75

        colorRegistry.add(new Color(89,  144, 17));   //  76
        colorRegistry.add(new Color(109, 176, 21));   //  77
        colorRegistry.add(new Color(127, 204, 25));   //  78
        colorRegistry.add(new Color(67,  108, 13));   //  79

        colorRegistry.add(new Color(170, 89,  116));  //  80
        colorRegistry.add(new Color(208, 109, 142));  //  81
        colorRegistry.add(new Color(242, 127, 165));  //  82
        colorRegistry.add(new Color(128, 67,  87));   //  83

        colorRegistry.add(new Color(53,  53,  53));   //  84
        colorRegistry.add(new Color(65,  65,  65));   //  85
        colorRegistry.add(new Color(76,  76,  76));   //  86
        colorRegistry.add(new Color(40,  40,  40));   //  87

        colorRegistry.add(new Color(108, 108, 108));  //  88
        colorRegistry.add(new Color(132, 132, 132));  //  89
        colorRegistry.add(new Color(153, 153, 153));  //  90
        colorRegistry.add(new Color(81,  81,  81));   //  91

        colorRegistry.add(new Color(53,  89,  108));  //  92
        colorRegistry.add(new Color(65,  109, 132));  //  93
        colorRegistry.add(new Color(76,  127, 153));  //  94
        colorRegistry.add(new Color(40,  67,  81));   //  95

        colorRegistry.add(new Color(89,  44,  125));  //  96
        colorRegistry.add(new Color(109, 54,  153));  //  97
        colorRegistry.add(new Color(127, 63,  178));  //  98
        colorRegistry.add(new Color(67,  33,  94));   //  99

        colorRegistry.add(new Color(36,  53,  125));  // 100
        colorRegistry.add(new Color(44,  65,  153));  // 101
        colorRegistry.add(new Color(51,  76,  178));  // 102
        colorRegistry.add(new Color(27,  40,  94));   // 103

        colorRegistry.add(new Color(72,  53,  36));   // 104
        colorRegistry.add(new Color(88,  65,  44));   // 105
        colorRegistry.add(new Color(102, 76,  51));   // 106
        colorRegistry.add(new Color(54,  40,  27));   // 107

        colorRegistry.add(new Color(72,  89,  36));   // 108
        colorRegistry.add(new Color(88,  109, 44));   // 109
        colorRegistry.add(new Color(102, 127, 51));   // 110
        colorRegistry.add(new Color(54,  67,  27));   // 111

        colorRegistry.add(new Color(108, 36,  36));   // 112
        colorRegistry.add(new Color(132, 44,  44));   // 113
        colorRegistry.add(new Color(153, 51,  51));   // 114
        colorRegistry.add(new Color(81,  27,  27));   // 115

        colorRegistry.add(new Color(17,  17,  17));   // 116
        colorRegistry.add(new Color(21,  21,  21));   // 117
        colorRegistry.add(new Color(25,  25,  25));   // 118
        colorRegistry.add(new Color(13,  13,  13));   // 119

        colorRegistry.add(new Color(176, 168, 54));   // 120
        colorRegistry.add(new Color(215, 205, 66));   // 121
        colorRegistry.add(new Color(250, 238, 77));   // 122
        colorRegistry.add(new Color(132, 126, 40));   // 123

        colorRegistry.add(new Color(64,  154, 150));  // 124
        colorRegistry.add(new Color(79,  188, 183));  // 125
        colorRegistry.add(new Color(92,  219, 213));  // 126
        colorRegistry.add(new Color(48,  115, 112));  // 127

        colorRegistry.add(new Color(52,  90,  180));  // 128
        colorRegistry.add(new Color(63,  110, 220));  // 129
        colorRegistry.add(new Color(74,  128, 255));  // 130
        colorRegistry.add(new Color(39,  67,  135));  // 131

        colorRegistry.add(new Color(0,   153, 40));   // 132
        colorRegistry.add(new Color(0,   187, 50));   // 133
        colorRegistry.add(new Color(0,   217, 58));   // 134
        colorRegistry.add(new Color(0,   114, 30));   // 135

        colorRegistry.add(new Color(91,  60,  34));   // 136
        colorRegistry.add(new Color(111, 74,  42));   // 137
        colorRegistry.add(new Color(129, 86,  49));   // 138
        colorRegistry.add(new Color(68,  45,  25));   // 139

        colorRegistry.add(new Color(79,  1,   0));    // 140
        colorRegistry.add(new Color(96,  1,   0));    // 141
        colorRegistry.add(new Color(112, 2,   0));    // 142
        colorRegistry.add(new Color(59,  1,   0));    // 143

        colorRegistry.add(new Color(147, 124, 113));  // 144
        colorRegistry.add(new Color(180, 152, 138));  // 145
        colorRegistry.add(new Color(209, 177, 161));  // 146
        colorRegistry.add(new Color(110, 93,  85));   // 147

        colorRegistry.add(new Color(112, 57,  25));   // 148
        colorRegistry.add(new Color(137, 70,  31));   // 149
        colorRegistry.add(new Color(159, 82,  36));   // 150
        colorRegistry.add(new Color(84,  43,  19));   // 151

        colorRegistry.add(new Color(105, 61,  76));   // 152
        colorRegistry.add(new Color(128, 75,  93));   // 153
        colorRegistry.add(new Color(149, 87,  108));  // 154
        colorRegistry.add(new Color(78,  46,  57));   // 155

        colorRegistry.add(new Color(79,  76,  97));   // 156
        colorRegistry.add(new Color(96,  93,  119));  // 157
        colorRegistry.add(new Color(112, 108, 138));  // 158
        colorRegistry.add(new Color(59,  57,  73));   // 159

        colorRegistry.add(new Color(131, 93,  25));   // 160
        colorRegistry.add(new Color(160, 114, 31));   // 161
        colorRegistry.add(new Color(186, 133, 36));   // 162
        colorRegistry.add(new Color(98,  70,  19));   // 163

        colorRegistry.add(new Color(72,  82,  37));   // 164
        colorRegistry.add(new Color(88,  100, 45));   // 165
        colorRegistry.add(new Color(103, 117, 53));   // 166
        colorRegistry.add(new Color(54,  61,  28));   // 167

        colorRegistry.add(new Color(112, 54,  55));   // 168
        colorRegistry.add(new Color(138, 66,  67));   // 169
        colorRegistry.add(new Color(160, 77,  78));   // 170
        colorRegistry.add(new Color(84,  40,  41));   // 171

        colorRegistry.add(new Color(40,  28,  24));   // 172
        colorRegistry.add(new Color(49,  35,  30));   // 173
        colorRegistry.add(new Color(57,  41,  35));   // 174
        colorRegistry.add(new Color(30,  21,  18));   // 175

        colorRegistry.add(new Color(95,  75,  69));   // 176
        colorRegistry.add(new Color(116, 92,  84));   // 177
        colorRegistry.add(new Color(135, 107, 98));   // 178
        colorRegistry.add(new Color(71,  56,  51));   // 179

        colorRegistry.add(new Color(61,  64,  64));   // 180
        colorRegistry.add(new Color(75,  79,  79));   // 181
        colorRegistry.add(new Color(87,  92,  92));   // 182
        colorRegistry.add(new Color(46,  48,  48));   // 183

        colorRegistry.add(new Color(86,  51,  62));   // 184
        colorRegistry.add(new Color(105, 62,  75));   // 185
        colorRegistry.add(new Color(122, 73,  88));   // 186
        colorRegistry.add(new Color(64,  38,  46));   // 187

        colorRegistry.add(new Color(53,  43,  64));   // 188
        colorRegistry.add(new Color(65,  53,  79));   // 189
        colorRegistry.add(new Color(76,  62,  92));   // 190
        colorRegistry.add(new Color(40,  32,  48));   // 191

        colorRegistry.add(new Color(53,  35,  24));   // 192
        colorRegistry.add(new Color(65,  43,  30));   // 193
        colorRegistry.add(new Color(76,  50,  35));   // 194
        colorRegistry.add(new Color(40,  26,  18));   // 195

        colorRegistry.add(new Color(53,  57,  29));   // 196
        colorRegistry.add(new Color(65,  70,  36));   // 197
        colorRegistry.add(new Color(76,  82,  42));   // 198
        colorRegistry.add(new Color(40,  43,  22));   // 199

        colorRegistry.add(new Color(100, 42,  32));   // 200
        colorRegistry.add(new Color(122, 51,  39));   // 201
        colorRegistry.add(new Color(142, 60,  46));   // 202
        colorRegistry.add(new Color(75,  31,  24));   // 203

        colorRegistry.add(new Color(26,  15,  11));   // 204
        colorRegistry.add(new Color(31,  18,  13));   // 205
        colorRegistry.add(new Color(37,  22,  16));   // 206
        colorRegistry.add(new Color(19,  11,  8));    // 207

        colorRegistry.add(new Color(133, 33,  34));   // 208
        colorRegistry.add(new Color(163, 41,  42));   // 209
        colorRegistry.add(new Color(189, 48,  49));   // 210
        colorRegistry.add(new Color(100, 25,  25));   // 211

        colorRegistry.add(new Color(104, 44,  68));   // 212
        colorRegistry.add(new Color(127, 54,  83));   // 213
        colorRegistry.add(new Color(148, 63,  97));   // 214
        colorRegistry.add(new Color(78,  33,  51));   // 215

        colorRegistry.add(new Color(64,  17,  20));   // 216
        colorRegistry.add(new Color(79,  21,  25));   // 217
        colorRegistry.add(new Color(92,  25,  29));   // 218
        colorRegistry.add(new Color(48,  13,  15));   // 219

        colorRegistry.add(new Color(15,  88,  94));   // 220
        colorRegistry.add(new Color(18,  108, 115));  // 221
        colorRegistry.add(new Color(22,  126, 134));  // 222
        colorRegistry.add(new Color(11,  66,  70));   // 223

        colorRegistry.add(new Color(40,  100, 98));   // 224
        colorRegistry.add(new Color(50,  122, 120));  // 225
        colorRegistry.add(new Color(58,  142, 140));  // 226
        colorRegistry.add(new Color(30,  75,  74));   // 227

        colorRegistry.add(new Color(60,  31,  43));   // 228
        colorRegistry.add(new Color(74,  37,  53));   // 229
        colorRegistry.add(new Color(86,  44,  62));   // 230
        colorRegistry.add(new Color(45,  23,  32));   // 231

        colorRegistry.add(new Color(14,  127, 93));   // 232
        colorRegistry.add(new Color(17,  155, 114));  // 233
        colorRegistry.add(new Color(20,  180, 133));  // 234
        colorRegistry.add(new Color(10,  95,  70));   // 235

        colorRegistry.add(new Color(70,  70,  70));   // 236
        colorRegistry.add(new Color(86,  86,  86));   // 237
        colorRegistry.add(new Color(100, 100, 100));  // 238
        colorRegistry.add(new Color(52,  52,  52));   // 239

        registryColorAmount = colorRegistry.size();

        // 比對相似度
        for (int r = 0 ; r < 64 ; r ++)
            for (int g = 0 ; g < 64 ; g ++)
                for (int b = 0 ; b < 64 ; b ++) {
                    double  similar;
                    Double  maxSimilar = null;
                    boolean hit;
                    byte    hitColorID = 0;
                    Color   colorA;
                    Color   colorB;
                    for (int colorID = 0, size = registryColorAmount ; colorID < size ; colorID++) {
                        colorA      = colorRegistry.get(colorID);
                        colorB      = new Color(r << 2, g << 2, b << 2);

                        if (r == g && g == b) {
                            similar     = getColorDistance(colorA, colorB);
                            hit         = maxSimilar == null || similar < maxSimilar;
                        } else {
                            similar     = getColorSemblance(colorA, colorB);
                            hit         = maxSimilar == null || similar > maxSimilar;
                        }

                        if (hit) {
                            maxSimilar = similar;
                            hitColorID = (byte) colorID;
                        }
                    }
                    colorIndexRGB[(r + (g << 6) + (b << 12))] = hitColorID >= 0 && hitColorID < 4 ? 119 : hitColorID;
                }


        colorRegistry.add(new Color(0,  0, 0, 0));  // 240
        colorRegistry.add(new Color(0,  0, 0, 0));  // 241
        colorRegistry.add(new Color(0,  0, 0, 0));  // 242
        colorRegistry.add(new Color(0,  0, 0, 0));  // 243

        colorRegistry.add(new Color(0,  0, 0, 0));  // 244
        colorRegistry.add(new Color(0,  0, 0, 0));  // 245
        colorRegistry.add(new Color(0,  0, 0, 0));  // 246
        colorRegistry.add(new Color(0,  0, 0, 0));  // 247

        colorRegistry.add(new Color(0,  0, 0, 0));  // 248
        colorRegistry.add(new Color(0,  0, 0, 0));  // 249
        colorRegistry.add(new Color(0,  0, 0, 0));  // 250
        colorRegistry.add(new Color(0,  0, 0, 0));  // 251

        colorRegistry.add(new Color(0,  0, 0, 0));  // 252
        colorRegistry.add(new Color(0,  0, 0, 0));  // 253
        colorRegistry.add(new Color(0,  0, 0, 0));  // 254
        colorRegistry.add(new Color(0,  0, 0, 0));  // 255
    }

    public static double square(double num) {
        return num * num;
    }
    public static double getColorDistance(Color colorA, Color colorB) {
        return square(colorA.getRed() - colorB.getRed()) + square(colorA.getGreen() - colorB.getGreen()) + square(colorA.getBlue() - colorB.getBlue());
    }
    public static double getColorSemblance(Color colorA, Color colorB) {
        return (255d - (Math.abs(colorA.getRed() - colorB.getRed()) * 255d * 0.297d + Math.abs(colorA.getGreen() - colorB.getGreen()) * 255d * 0.593d + Math.abs(colorA.getBlue() - colorB.getBlue()) * 255d * 11.0d / 100d)) / 255d;
    }


//    public int getRGBFromColorGroup(byte colorGroup) {
//        return MaterialMapColor.a[colorGroup].al/*rgb*/;
//    }

    public byte matchColor(Color color) {
        return matchColor(color.getRed(), color.getGreen(), color.getBlue());
    }
    public byte matchColor(int rgb) {
        return matchColor( rgb >> 16, rgb >> 8, rgb);
    }
    public byte matchColor(int r, int g, int b) {
        r = (r & 0xFF) >> 2;
        g = (g & 0xFF) >> 2;
        b = (b & 0xFF) >> 2;

        return this.colorIndexRGB[(r + (g << 6) + (b << 12))];
    }

    public Color getColorFromColorIndex(byte colorIndex) {
        if (colorIndex <= -9 || colorIndex >= 0) {
            return colorRegistry.get(colorIndex >= 0 ? colorIndex : colorIndex + 256);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getRegistryColorAmount() {
        return registryColorAmount;
    }
}
