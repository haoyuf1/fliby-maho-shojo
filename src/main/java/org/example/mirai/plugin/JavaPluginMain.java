package org.example.mirai.plugin;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 使用 Java 请把
 * {@code /src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin}
 * 文件内容改成 {@code org.example.mirai.plugin.JavaPluginMain} <br/>
 * 也就是当前主类全类名
 *
 * 使用 Java 可以把 kotlin 源集删除且不会对项目有影响
 *
 * 在 {@code settings.gradle.kts} 里改构建的插件名称、依赖库和插件版本
 *
 * 在该示例下的 {@link JvmPluginDescription} 修改插件名称，id 和版本等
 *
 * 可以使用 {@code src/test/kotlin/RunMirai.kt} 在 IDE 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

public final class JavaPluginMain extends JavaPlugin {
    public static final JavaPluginMain INSTANCE = new JavaPluginMain();
    private JavaPluginMain() {
        super(new JvmPluginDescriptionBuilder("com.fliby.mahoshojo", "0.1.0")
                .name("Fliby Maho Shojo")
                .info("キラキラ✨くるくる～")
                .author("ポッチャマ")
                .build());
    }
/**
 *计算人品的哈希函数
 *qq就是QQ号，Mirai监听到的是long
 *range就是期望这个每日哈希随机数的上限
 */
    private int getDailyHash(long qq, int range) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String ts = qq + new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        byte[] encodeHash = digest.digest(ts.getBytes());
        char[] hexTempHash = new char[2*encodeHash.length];
        for (int j = 0; j < encodeHash.length; j++) {
            int v = encodeHash[j] & 0xFF;
            hexTempHash[j * 2] =  hexArray[v >>> 4];
            hexTempHash[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String hexHash = new String(hexTempHash).replaceAll("\\D+", "").substring(0,8);
        Integer intHash = Integer.parseInt(hexHash);
        return intHash%range;
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        super.onLoad($this$onLoad);
    }

    @Override
    public void onEnable() {
        getLogger().info("日志");
        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
//        eventChannel.registerListenerHost(new DailyHash());
        eventChannel.subscribeAlways(GroupMessageEvent.class, g -> {
            //监听群消息
//            getLogger().info(g.getMessage().contentToString());
            String s = g.getMessage().contentToString();
            if (s.contains("今日人品")){
                long qq = 0;
//                Pattern pattern = Pattern.compile("今日人品\\[mirai:at:\\d+\\]");
//                Matcher matcher = pattern.matcher(s);
                if (s.equals("今日人品")) {
                    qq = g.getSender().getId();
                } else {
                    for (SingleMessage singleMessage : g.getMessage()){
                        if (singleMessage instanceof At){
                            qq = ((At) singleMessage).getTarget();
                        }
                    }
                }
                MessageChain msg = null;
                if(qq!=0) {
                    try {
                        msg = new At(qq).plus("今天的人品是").plus(Integer.toString(getDailyHash(qq, 101))).plus("!");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    g.getGroup().sendMessage(msg);
                }
            } else if (s.contains("今日老婆")){
                long qq = 0;
//                Pattern pattern = Pattern.compile("今日老婆\\[mirai:at:\\d+\\]");
//                Matcher matcher = pattern.matcher(s);
                if (s.equals("今日老婆")) {
                    qq = g.getSender().getId();
                } else {
                    for (SingleMessage singleMessage : g.getMessage()){
                        if (singleMessage instanceof At){
                            qq = ((At) singleMessage).getTarget();
                        }
                    }
                }
                MessageChain msg = null;
                if(qq!=0) {
                    try {
                        msg = new At(qq).plus("今天的老婆是「").plus(waifus.get(getDailyHash(qq, 374))).plus("」!");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    g.getGroup().sendMessage(msg);
                }
            } else if (s.contains("今日宝可梦")){
                long qq = 0;
//                Pattern pattern = Pattern.compile("今日老婆\\[mirai:at:\\d+\\]");
//                Matcher matcher = pattern.matcher(s);
                if (s.equals("今日宝可梦")) {
                    qq = g.getSender().getId();
                } else {
                    for (SingleMessage singleMessage : g.getMessage()){
                        if (singleMessage instanceof At){
                            qq = ((At) singleMessage).getTarget();
                        }
                    }
                }
                MessageChain msg = null;
                if(qq!=0) {
                    try {
                        msg = new At(qq).plus("今天的宝可梦是「").plus(pokemons.get(getDailyHash(qq, 907))).plus("」!");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    g.getGroup().sendMessage(msg);
                }
            } else if (s.equals("今日单抽")){
                try {
                    long qq = g.getSender().getId();
                    MessageChain msg = null;
                    String gacha = "";
                    int group = getDailyHash(qq, 101);
                    if (group<2){
                        gacha = fiveStars_2.get(getDailyHash(qq, fiveStars_2.size()));
                    } else if (group < 12) {
                        gacha = fiveStars_10.get(getDailyHash(qq, fiveStars_10.size()));
                    } else if (group < 32) {
                        gacha = fourStars_20.get(getDailyHash(qq, fourStars_20.size()));
                    } else {
                        gacha = threeStars_68.get(getDailyHash(qq,threeStars_68.size()));
                    }
                    String imgPath = "./icons/" + gacha + ".png";
                    ExternalResource res = ExternalResource.create(new File(imgPath));
                    Image image = contact.uploadImage(res);
                    res.close();
                    msg = new At(qq).plus("你今天抽到的是「").plus(gacha).plus("」!");
                    msg.plus(image);
                    g.getGroup().sendMessage(msg);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        eventChannel.subscribeAlways(FriendMessageEvent.class, f -> {
            //监听好友消息
            getLogger().info(f.getMessage().contentToString());
        });
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static List<String> waifus = Arrays.asList("赤红* レッド Red", "青绿* グリーン Blue", "大木博士 オーキド博士 Professor Oak", "奈奈美* ナナミ Daisy", "小刚 タケシ Brock", "小霞 カスミ Misty", "马志士 マチス Lt. Surge", "莉佳 エリカ Erika", "阿桔 キョウ Koga", "娜姿 ナツメ Sabrina", "夏伯 カツラ Blaine", "坂木 サカキ Giovanni", "科拿 カンナ Lorelei", "希巴 シバ Bruno", "菊子 キクコ Agatha", "阿渡 ワタル Lance", "正辉 マサキ Bill", "武德* タケノリ Koichi", "富士老人 フジろうじん Mr. Fuji", "巴奥巴* バオバ Baoba", "妈妈 ママ Mom", "模仿少女 モノマネむすめ Copycat", "老人 おじいさん Old man", "宝可梦中心的姐姐 ポケモンセンターナース Pokémon Center Nurse", "姓名鉴定师 せいめいはんだんし Name Rater", "武藏 ムサシ Jessie", "小次郎 コジロウ James", "乔伊* ジョーイ Nurse Joy", "君莎* ジュンサー Officer Jenny", "阿响* ヒビキ Ethan", "小银* シルバー Silver", "空木博士 ウツギ博士 Professor Elm", "阿速 ハヤト Falkner", "阿笔 ツクシ Bugsy", "小茜 アカネ Whitney", "松叶 マツバ Morty", "阿四 シジマ Chuck", "阿蜜 ミカン Jasmine", "柳伯 ヤナギ Pryce", "小椿 イブキ Clair", "一树 イツキ Will", "梨花 カリン Karen", "阿杏 アンズ Janine", "阿波罗* アポロ Archer", "雅典娜* アテナ Ariana", "兰斯* ランス Proton", "拉姆达* ラムダ Petrel", "初始 ジョバンニ Earl Dervish", "钢铁 ガンテツ Kurt", "五月 サツキ Miki", "桃桃 スモモ／コモモ Kuni", "玉绪 タマオ Tamao", "小梅 コウメ Naoko", "樱花 サクラ Sayo", "光圣 コウセイ Li", "妈妈 ママ Mom", "宝可梦爷爷 ポケモンおじいさん Mr. Pokémon", "胡桃 クルミ DJ Mary", "莉莉诗 リリス Lily", "黄杨 ツゲ Reed", "洋苏 セージ Ben", "清明 コージ Fern", "光亮 ヒカル／ひかる Cal", "合欢博士 ネムノキ博士 Professor Silktree", "信彦 ノブヒコ Kiyo", "星琪怡 ツキコ Monica", "星琪儿 ヒコ Tuscany", "星琪山 ミズオ Wesley", "星琪思 モクオ Arthur", "星琪舞 カネコ Frieda", "星琪柳 ツチオ Santos", "星琪天 ニチオ Sunny", "遗忘爷爷 わすれオヤジ Move Deleter", "小哥 おにいさん Dude", "克丽丝* クリス Kris", "水京 ミナキ Eusine", "葵妍 アオイ Buena", "龙之长老 ドラゴンつかいいちぞくの长老さま Master of the Dragon Tamer clan", "小悠 ユウキ Brendan", "小遥 ハルカ May", "小田卷博士 オダマキ博士 Professor Birch", "满充 ミツル Wally", "阿满 ミチル Wanda", "杜娟 ツツジ Roxanne", "藤树 トウキ Brawly", "铁旋 テッセン Wattson", "亚莎 アスナ Flannery", "千里 センリ Norman", "娜琪 ナギ Winona", "小枫 フウ Tate", "小南 ラン Liza", "米可利 ミクリ Wallace", "花月 カゲツ Sidney", "芙蓉 フヨウ Phoebe", "波妮 プリム Glacia", "源治 ゲンジ Drake", "大吾 ダイゴ Stone", "库斯诺吉馆长 クスノキ館長 Captain Stern", "都贺 ツガ Dock", "兹伏奇社长 ツワブキ社長 Mr. Stone", "真由美 マユミ Brigette", "赤焰松 マツブサ Maxie", "火村 ホムラ Tabitha", "火雁 カガリ Courtney", "水梧桐 アオギリ Archie", "阿泉 イズミ Shelly", "阿潮 ウシオ Matt", "哈奇老人 ハギ老人 Mr. Briney", "索蓝斯博士 ソライシ博士 Professor Cozmo", "妈妈 ママ Mom", "连胜晴彦 カチヌキ ハルヒコ Victor Winstrate", "连胜安江 カチヌキ ヤスエ Victoria Winstrate", "连胜小雪 カチヌキ アキ Vivi Winstrate", "连胜光代 カチヌキ ミツヨ Vicky Winstrate", "连胜良平 カチヌキ リョウヘイ Vito Winstrate", "玛莉 マリ Gabby", "小戴 ダイ Ty", "风野 カゼノ Rydel", "小桐 キリ Kiri", "招式教学狂 わざおしえマニア Move Reminder", "亚当 アダン Juan", "亚希达 エニシダ Scott", "达拉 ダツラ Noland", "黄瓜香 コゴミ Greta", "希尔斯 ヒース Tucker", "小蓟 アザミ Lucy", "宇康 ウコン Spenser", "神代 ジンダイ Brandon", "莉拉 リラ Anabel", "明辉 コウキ Lucas", "小光 ヒカリ Dawn", "阿驯* ジュン Barry", "山梨博士 ナナカマド博士 Professor Rowan", "彩子 アヤコ Johanna", "瓢太 ヒョウタ Roark", "菜种 ナタネ Gardenia", "阿李 スモモ Maylene", "吉宪 マキシ Crasher Wake", "梅丽莎 メリッサ Fantina", "东瓜 トウガン Byron", "小菘 スズナ Candice", "电次 デンジ Volkner", "阿柳 リョウ Aaron", "菊野 キクノ Bertha", "大叶 オーバ Flint", "悟松 ゴヨウ Lucian", "竹兰 シロナ Cynthia", "芽米 モミ Cheryl", "麦儿 ミル Mira", "亚玄 ゲン Riley", "米依 マイ Marley", "麦可 バク Buck", "赤日 アカギ Cyrus", "伙星 マーズ Mars", "岁星 ジュピター Jupiter", "镇星 サターン Saturn", "桄榔 クロツグ Palmer", "龙婆婆 タツばあさん Wilma", "水木 ミズキ Bebe", "现慕 ウラヤマ Mr. Backlot", "地下大叔 ちかおじさん Underground Man", "多多罗 タタラ Mr. Fuego", "足迹博士 あしあとはかせ Dr. Footstep", "慷慨大叔 ミスター・グッズ Mister Goods", "波木 ナミキ Eldritch", "毕克 ビック Dexter", "蜜蜜 ミミィ Keira", "佐助 サスケ Jordan", "文子 フミコ Julia", "滨名 ハマナ Roseanne", "自行车人力 じてんしゃ じんりき Rad Rickshaw", "琴音 コトネ Lyra", "阿结 ケジメ Maximo", "大拳 コブシ Magnus", "斗也 トウヤ Hilbert", "斗子 トウコ Hilda", "黑连 チェレン Cheren", "白露 ベル Bianca", "红豆杉博士 アララギ博士 Professor Juniper", "红豆杉博士的爸爸 アララギパパ Cedric Juniper", "真菰 マコモ Fennel", "松露 ショウロ Amanita", "天桐 デント Cilan", "伯特 ポッド Chili", "寇恩 コーン Cress", "芦荟 アロエ Lenora", "木立 キダチ Hawes", "亚堤 アーティ Burgh", "小菊儿 カミツレ Elesa", "菊老大 ヤーコン Clay", "风露 フウロ Skyla", "哈奇库 ハチク Brycen", "艾莉丝 アイリス Iris", "夏卡 シャガ Drayden", "婉龙 シキミ Shauntal", "越橘 ギーマ Grimsley", "连武 レンブ Marshal", "阿戴克 アデク Alder", "北尚 ノボリ Ingo", "南厦 クダリ Emmet", "Ｎ Ｎ N", "魁奇思 ゲーチス Ghetsis", "罗德 ロット Rood", "斯姆拉 スムラ Bronius", "杰洛 ジャロ Giallo", "约格斯 リョクシ Ryoku", "阿苏拉 アスラ Gorm", "维奥 ヴィオ Zinzolin", "巴贝娜 バーベナ Concordia", "荷莲娜 ヘレナ Anthea", "黑暗铁三角 ダークトリニティ Shadow Triad", "妈妈 ママ Mom", "森本 モリモト Morimoto", "西野 ニシノ Nishino", "紫檀 シタン Loblolly", "共平 キョウヘイ Nate", "鸣依 メイ Rosa", "阿修 ヒュウ Hugh", "霍米加 ホミカ Roxie", "霍米加的爸爸 ホミカパパ Pop Roxie", "西子伊 シズイ Marlon", "阿克罗玛 アクロマ Colress", "蕃石郎 バンジロウ Benga", "琉璃 ルリ Yancy", "阿铁 テツ Curtis", "卡鲁穆 カルム Calem", "莎莉娜 セレナ Serena", "提耶鲁诺 ティエルノ Tierno", "多罗巴 トロバ Trevor", "莎娜 サナ Shauna", "布拉塔诺博士 プラターヌ博士 Professor Augustine Sycamore", "紫罗兰 ビオラ Viola", "查克洛 ザクロ Grant", "可尔妮 コルニ Korrina", "福爷 フクジ Ramos", "希特隆 シトロン Clemont", "玛绣 マーシュ Valerie", "葛吉花 ゴジカ Olympia", "得抚 ウルップ Wulfric", "志米 ズミ Siebold", "朵拉塞娜 ドラセナ Drasna", "雁铠 ガンピ Wikstrom", "帕琦拉 パキラ Malva", "卡露妮 カルネ Diantha", "库瑟洛斯奇 クセロシキ Xerosic", "茉蜜姬 モミジ  Mable", "芭菈 バラ Bryony", "克蕾儿 コレア Celosia", "阿可碧 アケビ Aliana", "弗拉达利 フラダリ Lysandre", "ＡＺ ＡＺ ＡＺ", "吉娜 ジーナ Sina", "德克希欧 デクシオ Dexio", "三色堇 パンジー Alexa", "萨琪 サキ Grace", "柚丽嘉 ユリーカ Bonnie", "酷罗凯尔 クロケア Cassius", "玛琪艾儿 艾丝普莉 マチエール エスプリ Emma Essentia", "可可布尔 コンコンブル Gurkinn", "夜妮 ラニュイ Nita", "夕丝 ルスワール Evelyn", "昼珠 ラジュルネ Dana", "朝蜜 ルミタン Morgan", "阿卡马洛 アカマロ Chalmers", "凡篆 サカサ Inver", "基利 ギリー Aarune", "琉琪亚 ルチア Lisia", "希嘉娜 ヒガナ Zinnia", "葛蔓 カズラ Chaz", "茴璇 メグル Circie", "小阳* ヨウ Elio", "美月* ミヅキ Selene", "库库伊博士 ククイ博士 Professor Kukui", "妈妈 ママ Mom", "芭内特博士* バーネット博士 Professor Burnet", "莉莉艾 リーリエ Lillie", "哈乌 ハウ Hau", "伊利马 イリマ Ilima", "水莲 スイレン Lana", "玛奥 マオ Mallow", "马玛内 マーマネ Sophocles", "卡奇 カキ Kiawe", "阿塞萝拉 アセロラ Acerola", "茉莉 マツリカ Mina", "格拉吉欧 グラジオ Gladion", "布尔美丽 プルメリ Plumeria", "古兹马 グズマ Guzma", "露莎米奈 ルザミーネ Lusamine", "扎奥博 ザオボー Faba", "碧珂 ビッケ Wicke", "成也・大木 ナリヤ・オーキド Samson Oak", "哈拉 ハラ Hala", "丽姿 ライチ Olivia", "默丹 クチナシ Nanu", "哈普乌 ハプウ Hapu", "卡希丽 カヒリ Kahili", "马睿因 マーレイン Molayne", "龙葵 リュウキ Ryuki", "莫恩 モーン Mohn", "诗婷 ホウ Harper", "诗涵 スイ Sarah", "小驱* カケル Chase", "步美* アユミ Elaine", "小进* シン Trace", "碧蓝 ブルー Green", "小胜* マサル Victor", "小优* ユウリ Gloria", "妈妈 おかあさん Mum", "赫普 ホップ Hop", "丹帝 ダンデ Leon", "索妮亚 ソニア Sonia", "木兰博士 マグノリア博士 ‎ Professor Magnolia", "彼特 ビート Bede", "玛俐 マリィ Marnie", "洛兹 ローズ Rose", "奥利薇 オリーヴ Oleana", "亚洛 ヤロー Milo", "露璃娜 ルリナ Nessa", "卡芜 カブ Kabu", "彩豆 サイトウ Bea", "欧尼奥 オニオン Allister", "波普菈 ポプラ Opal", "玛瓜 マクワ Gordie", "美蓉 メロン Melony", "聂梓 ネズ Piers", "奇巴纳 キバナ Raihan", "阿球 ボールガイ Ball Guy", "邓培 ダンペイ Dan", "茂诗 ウカッツ Cara Liss", "索德 ソッド Sordward", "西尔迪 シルディ Shielbert", "克拉拉 クララ Klara", "赛宝利 セイボリー Avery", "马士德 マスタード Mustard", "蜜叶 ミツバ Honey", "海德 ハイド Hyde", "皮欧尼 ピオニー Peony", "夏科娅 シャクヤ Peonia", "明耀 テル Rei", "小照 ショウ Akari", "拉苯博士 ラベン博士 Professor Laventon", "星月 シマボシ Cyllene", "马加木 デンボク Kamado", "刚石 セキ Adaman", "珠贝 カイ Irida", "望罗 ウォロ Volo", "吾思 コギト Cogita", "石月 ムベ Beni", "阿米 ヨネ Mai", "菊伊 キクイ Lian", "夕蒲 ユウガオ Calaba", "火夏 ヒナツ Arezu", "阿芒 ススキ Iscan", "瓜娜 ガラナ Palina", "木春 ツバキ Melli", "滨廉 ハマレンゲ Gaeric", "山葵 ワサビ Sabi", "银仁 ギンナン Ginter", "贝里菈 ペリーラ Zisu", "晓白 タイサイ Choy", "纱珑 シャロン Anthe", "菜华 ナバナ Colza", "茶花 サザンカ Sanqua", "杵儿 キネ Pesselle", "桃发 タオファ Tao Hua", "亚白 ハク Rye", "阿松 オマツ Charm", "阿竹 オタケ Clover", "阿梅 オウメ Coin", "翠莉 ツイリ Tuli", "奥琳博士 オーリム博士 Professor Sada", "弗图博士 フトゥー博士 Professor Turo", "妮莫 ネモ Nemona");
    private final static List<String> pokemons = Arrays.asList("妙蛙种子 フシギダネ Bulbasaur", "妙蛙草 フシギソウ Ivysaur", "妙蛙花 フシギバナ Venusaur", "小火龙 ヒトカゲ Charmander", "火恐龙 リザード Charmeleon", "喷火龙 リザードン Charizard", "杰尼龟 ゼニガメ Squirtle", "卡咪龟 カメール Wartortle", "水箭龟 カメックス Blastoise", "绿毛虫 キャタピー Caterpie", "铁甲蛹 トランセル Metapod", "巴大蝶 バタフリー Butterfree", "独角虫 ビードル Weedle", "铁壳蛹 コクーン Kakuna", "大针蜂 スピアー Beedrill", "波波 ポッポ Pidgey", "比比鸟 ピジョン Pidgeotto", "大比鸟 ピジョット Pidgeot", "小拉达 コラッタ Rattata", "拉达 ラッタ Raticate", "烈雀 オニスズメ Spearow", "大嘴雀 オニドリル Fearow", "阿柏蛇 アーボ Ekans", "阿柏怪 アーボック Arbok", "皮卡丘 ピカチュウ Pikachu", "雷丘 ライチュウ Raichu", "穿山鼠 サンド Sandshrew", "穿山王 サンドパン Sandslash", "尼多兰 ニドラン♀ Nidoran♀", "尼多娜 ニドリーナ Nidorina", "尼多后 ニドクイン Nidoqueen", "尼多朗 ニドラン♂ Nidoran♂", "尼多力诺 ニドリーノ Nidorino", "尼多王 ニドキング Nidoking", "皮皮* ピッピ Clefairy", "皮可西* ピクシー Clefable", "六尾 ロコン Vulpix", "九尾 キュウコン Ninetales", "胖丁* プリン Jigglypuff", "胖可丁* プクリン Wigglytuff", "超音蝠 ズバット Zubat", "大嘴蝠 ゴルバット Golbat", "走路草 ナゾノクサ Oddish", "臭臭花 クサイハナ Gloom", "霸王花 ラフレシア Vileplume", "派拉斯 パラス Paras", "派拉斯特 パラセクト Parasect", "毛球 コンパン Venonat", "摩鲁蛾 モルフォン Venomoth", "地鼠 ディグダ Diglett", "三地鼠 ダグトリオ Dugtrio", "喵喵 ニャース Meowth", "猫老大 ペルシアン Persian", "可达鸭 コダック Psyduck", "哥达鸭 ゴルダック Golduck", "猴怪 マンキー Mankey", "火暴猴 オコリザル Primeape", "卡蒂狗 ガーディ Growlithe", "风速狗 ウインディ Arcanine", "蚊香蝌蚪 ニョロモ Poliwag", "蚊香君 ニョロゾ Poliwhirl", "蚊香泳士 ニョロボン Poliwrath", "凯西 ケーシィ Abra", "勇基拉 ユンゲラー Kadabra", "胡地 フーディン Alakazam", "腕力 ワンリキー Machop", "豪力 ゴーリキー Machoke", "怪力 カイリキー Machamp", "喇叭芽 マダツボミ Bellsprout", "口呆花 ウツドン Weepinbell", "大食花 ウツボット Victreebel", "玛瑙水母 メノクラゲ Tentacool", "毒刺水母 ドククラゲ Tentacruel", "小拳石 イシツブテ Geodude", "隆隆石 ゴローン Graveler", "隆隆岩 ゴローニャ Golem", "小火马 ポニータ Ponyta", "烈焰马 ギャロップ Rapidash", "呆呆兽 ヤドン Slowpoke", "呆壳兽 ヤドラン Slowbro", "小磁怪* コイル Magnemite", "三合一磁怪* レアコイル Magneton", "大葱鸭 カモネギ Farfetch'd", "嘟嘟 ドードー Doduo", "嘟嘟利 ドードリオ Dodrio", "小海狮 パウワウ Seel", "白海狮 ジュゴン Dewgong", "臭泥 ベトベター Grimer", "臭臭泥 ベトベトン Muk", "大舌贝 シェルダー Shellder", "刺甲贝 パルシェン Cloyster", "鬼斯 ゴース Gastly", "鬼斯通 ゴースト Haunter", "耿鬼 ゲンガー Gengar", "大岩蛇 イワーク Onix", "催眠貘 スリープ Drowzee", "引梦貘人 スリーパー Hypno", "大钳蟹 クラブ Krabby", "巨钳蟹 キングラー Kingler", "霹雳电球 ビリリダマ Voltorb", "顽皮雷弹 マルマイン Electrode", "蛋蛋 タマタマ Exeggcute", "椰蛋树 ナッシー Exeggutor", "卡拉卡拉 カラカラ Cubone", "嘎啦嘎啦 ガラガラ Marowak", "飞腿郎 サワムラー Hitmonlee", "快拳郎 エビワラー Hitmonchan", "大舌头 ベロリンガ Lickitung", "瓦斯弹 ドガース Koffing", "双弹瓦斯 マタドガス Weezing", "独角犀牛 サイホーン Rhyhorn", "钻角犀兽 サイドン Rhydon", "吉利蛋 ラッキー Chansey", "蔓藤怪 モンジャラ Tangela", "袋兽 ガルーラ Kangaskhan", "墨海马 タッツー Horsea", "海刺龙 シードラ Seadra", "角金鱼 トサキント Goldeen", "金鱼王 アズマオウ Seaking", "海星星 ヒトデマン Staryu", "宝石海星 スターミー Starmie", "魔墙人偶* バリヤード Mr. Mime", "飞天螳螂 ストライク Scyther", "迷唇姐 ルージュラ Jynx", "电击兽 エレブー Electabuzz", "鸭嘴火兽 ブーバー Magmar", "凯罗斯 カイロス Pinsir", "肯泰罗 ケンタロス Tauros", "鲤鱼王 コイキング Magikarp", "暴鲤龙 ギャラドス Gyarados", "拉普拉斯 ラプラス Lapras", "百变怪 メタモン Ditto", "伊布 イーブイ Eevee", "水伊布 シャワーズ Vaporeon", "雷伊布 サンダース Jolteon", "火伊布 ブースター Flareon", "多边兽 ポリゴン Porygon", "菊石兽 オムナイト Omanyte", "多刺菊石兽 オムスター Omastar", "化石盔 カブト Kabuto", "镰刀盔 カブトプス Kabutops", "化石翼龙 プテラ Aerodactyl", "卡比兽 カビゴン Snorlax", "急冻鸟 フリーザー Articuno", "闪电鸟 サンダー Zapdos", "火焰鸟 ファイヤー Moltres", "迷你龙 ミニリュウ Dratini", "哈克龙 ハクリュー Dragonair", "快龙 カイリュー Dragonite", "超梦 ミュウツー Mewtwo", "梦幻 ミュウ Mew", "菊草叶 チコリータ Chikorita", "月桂叶 ベイリーフ Bayleef", "大竺葵 メガニウム Meganium", "火球鼠 ヒノアラシ Cyndaquil", "火岩鼠 マグマラシ Quilava", "火暴兽 バクフーン Typhlosion", "小锯鳄 ワニノコ Totodile", "蓝鳄 アリゲイツ Croconaw", "大力鳄 オーダイル Feraligatr", "尾立 オタチ Sentret", "大尾立 オオタチ Furret", "咕咕 ホーホー Hoothoot", "猫头夜鹰 ヨルノズク Noctowl", "芭瓢虫 レディバ Ledyba", "安瓢虫 レディアン Ledian", "圆丝蛛 イトマル Spinarak", "阿利多斯 アリアドス Ariados", "叉字蝠 クロバット Crobat", "灯笼鱼 チョンチー Chinchou", "电灯怪 ランターン Lanturn", "皮丘 ピチュー Pichu", "皮宝宝* ピィ Cleffa", "宝宝丁* ププリン Igglybuff", "波克比* トゲピー Togepi", "波克基古* トゲチック Togetic", "天然雀 ネイティ Natu", "天然鸟 ネイティオ Xatu", "咩利羊 メリープ Mareep", "茸茸羊 モココ Flaaffy", "电龙 デンリュウ Ampharos", "美丽花 キレイハナ Bellossom", "玛力露* マリル Marill", "玛力露丽* マリルリ Azumarill", "树才怪 ウソッキー Sudowoodo", "蚊香蛙皇 ニョロトノ Politoed", "毽子草 ハネッコ Hoppip", "毽子花 ポポッコ Skiploom", "毽子棉 ワタッコ Jumpluff", "长尾怪手 エイパム Aipom", "向日种子 ヒマナッツ Sunkern", "向日花怪 キマワリ Sunflora", "蜻蜻蜓 ヤンヤンマ Yanma", "乌波 ウパー Wooper", "沼王 ヌオー Quagsire", "太阳伊布 エーフィ Espeon", "月亮伊布 ブラッキー Umbreon", "黑暗鸦 ヤミカラス Murkrow", "呆呆王 ヤドキング Slowking", "梦妖 ムウマ Misdreavus", "未知图腾 アンノーン Unown", "果然翁 ソーナンス Wobbuffet", "麒麟奇 キリンリキ Girafarig", "榛果球 クヌギダマ Pineco", "佛烈托斯 フォレトス Forretress", "土龙弟弟 ノコッチ Dunsparce", "天蝎 グライガー Gligar", "大钢蛇 ハガネール Steelix", "布鲁* ブルー Snubbull", "布鲁皇* グランブル Granbull", "千针鱼 ハリーセン Qwilfish", "巨钳螳螂 ハッサム Scizor", "壶壶 ツボツボ Shuckle", "赫拉克罗斯 ヘラクロス Heracross", "狃拉 ニューラ Sneasel", "熊宝宝 ヒメグマ Teddiursa", "圈圈熊 リングマ Ursaring", "熔岩虫 マグマッグ Slugma", "熔岩蜗牛 マグカルゴ Magcargo", "小山猪 ウリムー Swinub", "长毛猪 イノムー Piloswine", "太阳珊瑚 サニーゴ Corsola", "铁炮鱼 テッポウオ Remoraid", "章鱼桶 オクタン Octillery", "信使鸟 デリバード Delibird", "巨翅飞鱼 マンタイン Mantine", "盔甲鸟 エアームド Skarmory", "戴鲁比 デルビル Houndour", "黑鲁加 ヘルガー Houndoom", "刺龙王 キングドラ Kingdra", "小小象 ゴマゾウ Phanpy", "顿甲 ドンファン Donphan", "多边兽Ⅱ ポリゴン２ Porygon2", "惊角鹿 オドシシ Stantler", "图图犬 ドーブル Smeargle", "无畏小子 バルキー Tyrogue", "战舞郎 カポエラー Hitmontop", "迷唇娃 ムチュール Smoochum", "电击怪 エレキッド Elekid", "鸭嘴宝宝 ブビィ Magby", "大奶罐 ミルタンク Miltank", "幸福蛋 ハピナス Blissey", "雷公 ライコウ Raikou", "炎帝 エンテイ Entei", "水君 スイクン Suicune", "幼基拉斯 ヨーギラス Larvitar", "沙基拉斯 サナギラス Pupitar", "班基拉斯 バンギラス Tyranitar", "洛奇亚 ルギア Lugia", "凤王 ホウオウ Ho-Oh", "时拉比 セレビィ Celebi", "木守宫 キモリ Treecko", "森林蜥蜴 ジュプトル Grovyle", "蜥蜴王 ジュカイン Sceptile", "火稚鸡 アチャモ Torchic", "力壮鸡 ワカシャモ Combusken", "火焰鸡 バシャーモ Blaziken", "水跃鱼 ミズゴロウ Mudkip", "沼跃鱼 ヌマクロー Marshtomp", "巨沼怪 ラグラージ Swampert", "土狼犬 ポチエナ Poochyena", "大狼犬 グラエナ Mightyena", "蛇纹熊 ジグザグマ Zigzagoon", "直冲熊 マッスグマ Linoone", "刺尾虫 ケムッソ Wurmple", "甲壳茧 カラサリス Silcoon", "狩猎凤蝶 アゲハント Beautifly", "盾甲茧 マユルド Cascoon", "毒粉蛾 ドクケイル Dustox", "莲叶童子 ハスボー Lotad", "莲帽小童 ハスブレロ Lombre", "乐天河童 ルンパッパ Ludicolo", "橡实果 タネボー Seedot", "长鼻叶 コノハナ Nuzleaf", "狡猾天狗 ダーテング Shiftry", "傲骨燕 スバメ Taillow", "大王燕 オオスバメ Swellow", "长翅鸥 キャモメ Wingull", "大嘴鸥 ペリッパー Pelipper", "拉鲁拉丝* ラルトス Ralts", "奇鲁莉安* キルリア Kirlia", "沙奈朵* サーナイト Gardevoir", "溜溜糖球 アメタマ Surskit", "雨翅蛾 アメモース Masquerain", "蘑蘑菇 キノココ Shroomish", "斗笠菇 キノガッサ Breloom", "懒人獭 ナマケロ Slakoth", "过动猿 ヤルキモノ Vigoroth", "请假王 ケッキング Slaking", "土居忍士 ツチニン Nincada", "铁面忍者 テッカニン Ninjask", "脱壳忍者 ヌケニン Shedinja", "咕妞妞 ゴニョニョ Whismur", "吼爆弹 ドゴーム Loudred", "爆音怪 バクオング Exploud", "幕下力士 マクノシタ Makuhita", "铁掌力士 ハリテヤマ Hariyama", "露力丽* ルリリ Azurill", "朝北鼻 ノズパス Nosepass", "向尾喵 エネコ Skitty", "优雅猫 エネコロロ Delcatty", "勾魂眼 ヤミラミ Sableye", "大嘴娃* クチート Mawile", "可可多拉 ココドラ Aron", "可多拉 コドラ Lairon", "波士可多拉 ボスゴドラ Aggron", "玛沙那 アサナン Meditite", "恰雷姆 チャーレム Medicham", "落雷兽 ラクライ Electrike", "雷电兽 ライボルト Manectric", "正电拍拍 プラスル Plusle", "负电拍拍 マイナン Minun", "电萤虫 バルビート Volbeat", "甜甜萤 イルミーゼ Illumise", "毒蔷薇 ロゼリア Roselia", "溶食兽 ゴクリン Gulpin", "吞食兽 マルノーム Swalot", "利牙鱼 キバニア Carvanha", "巨牙鲨 サメハダー Sharpedo", "吼吼鲸 ホエルコ Wailmer", "吼鲸王 ホエルオー Wailord", "呆火驼 ドンメル Numel", "喷火驼 バクーダ Camerupt", "煤炭龟 コータス Torkoal", "跳跳猪 バネブー Spoink", "噗噗猪 ブーピッグ Grumpig", "晃晃斑 パッチール Spinda", "大颚蚁 ナックラー Trapinch", "超音波幼虫 ビブラーバ Vibrava", "沙漠蜻蜓 フライゴン Flygon", "刺球仙人掌 サボネア Cacnea", "梦歌仙人掌 ノクタス Cacturne", "青绵鸟 チルット Swablu", "七夕青鸟 チルタリス Altaria", "猫鼬斩 ザングース Zangoose", "饭匙蛇 ハブネーク Seviper", "月石 ルナトーン Lunatone", "太阳岩 ソルロック Solrock", "泥泥鳅 ドジョッチ Barboach", "鲶鱼王 ナマズン Whiscash", "龙虾小兵 ヘイガニ Corphish", "铁螯龙虾 シザリガー Crawdaunt", "天秤偶 ヤジロン Baltoy", "念力土偶 ネンドール Claydol", "触手百合 リリーラ Lileep", "摇篮百合 ユレイドル Cradily", "太古羽虫 アノプス Anorith", "太古盔甲 アーマルド Armaldo", "丑丑鱼 ヒンバス Feebas", "美纳斯 ミロカロス Milotic", "飘浮泡泡 ポワルン Castform", "变隐龙 カクレオン Kecleon", "怨影娃娃 カゲボウズ Shuppet", "诅咒娃娃 ジュペッタ Banette", "夜巡灵 ヨマワル Duskull", "彷徨夜灵 サマヨール Dusclops", "热带龙 トロピウス Tropius", "风铃铃 チリーン Chimecho", "阿勃梭鲁 アブソル Absol", "小果然 ソーナノ Wynaut", "雪童子 ユキワラシ Snorunt", "冰鬼护 オニゴーリ Glalie", "海豹球 タマザラシ Spheal", "海魔狮 トドグラー Sealeo", "帝牙海狮 トドゼルガ Walrein", "珍珠贝 パールル Clamperl", "猎斑鱼 ハンテール Huntail", "樱花鱼 サクラビス Gorebyss", "古空棘鱼 ジーランス Relicanth", "爱心鱼 ラブカス Luvdisc", "宝贝龙 タツベイ Bagon", "甲壳龙 コモルー Shelgon", "暴飞龙 ボーマンダ Salamence", "铁哑铃 ダンバル Beldum", "金属怪 メタング Metang", "巨金怪 メタグロス Metagross", "雷吉洛克 レジロック Regirock", "雷吉艾斯 レジアイス Regice", "雷吉斯奇鲁 レジスチル Registeel", "拉帝亚斯 ラティアス Latias", "拉帝欧斯 ラティオス Latios", "盖欧卡 カイオーガ Kyogre", "固拉多 グラードン Groudon", "烈空坐 レックウザ Rayquaza", "基拉祈 ジラーチ Jirachi", "代欧奇希斯 デオキシス Deoxys", "草苗龟 ナエトル Turtwig", "树林龟 ハヤシガメ Grotle", "土台龟 ドダイトス Torterra", "小火焰猴 ヒコザル Chimchar", "猛火猴 モウカザル Monferno", "烈焰猴 ゴウカザル Infernape", "波加曼 ポッチャマ Piplup", "波皇子 ポッタイシ Prinplup", "帝王拿波 エンペルト Empoleon", "姆克儿 ムックル Starly", "姆克鸟 ムクバード Staravia", "姆克鹰 ムクホーク Staraptor", "大牙狸 ビッパ Bidoof", "大尾狸 ビーダル Bibarel", "圆法师 コロボーシ Kricketot", "音箱蟀 コロトック Kricketune", "小猫怪 コリンク Shinx", "勒克猫 ルクシオ Luxio", "伦琴猫 レントラー Luxray", "含羞苞 スボミー Budew", "罗丝雷朵 ロズレイド Roserade", "头盖龙 ズガイドス Cranidos", "战槌龙 ラムパルド Rampardos", "盾甲龙 タテトプス Shieldon", "护城龙 トリデプス Bastiodon", "结草儿 ミノムッチ Burmy", "结草贵妇 ミノマダム Wormadam", "绅士蛾 ガーメイル Mothim", "三蜜蜂 ミツハニー Combee", "蜂女王 ビークイン Vespiquen", "帕奇利兹 パチリス Pachirisu", "泳圈鼬 ブイゼル Buizel", "浮潜鼬 フローゼル Floatzel", "樱花宝 チェリンボ Cherubi", "樱花儿 チェリム Cherrim", "无壳海兔 カラナクシ Shellos", "海兔兽 トリトドン Gastrodon", "双尾怪手 エテボース Ambipom", "飘飘球 フワンテ Drifloon", "随风球 フワライド Drifblim", "卷卷耳 ミミロル Buneary", "长耳兔 ミミロップ Lopunny", "梦妖魔 ムウマージ Mismagius", "乌鸦头头 ドンカラス Honchkrow", "魅力喵 ニャルマー Glameow", "东施喵 ブニャット Purugly", "铃铛响 リーシャン Chingling", "臭鼬噗 スカンプー Stunky", "坦克臭鼬 スカタンク Skuntank", "铜镜怪 ドーミラー Bronzor", "青铜钟 ドータクン Bronzong", "盆才怪 ウソハチ Bonsly", "魔尼尼* マネネ Mime Jr.", "小福蛋 ピンプク Happiny", "聒噪鸟 ペラップ Chatot", "花岩怪 ミカルゲ Spiritomb", "圆陆鲨 フカマル Gible", "尖牙陆鲨 ガバイト Gabite", "烈咬陆鲨 ガブリアス Garchomp", "小卡比兽 ゴンベ Munchlax", "利欧路 リオル Riolu", "路卡利欧 ルカリオ Lucario", "沙河马 ヒポポタス Hippopotas", "河马兽 カバルドン Hippowdon", "钳尾蝎 スコルピ Skorupi", "龙王蝎 ドラピオン Drapion", "不良蛙 グレッグル Croagunk", "毒骷蛙 ドクロッグ Toxicroak", "尖牙笼 マスキッパ Carnivine", "荧光鱼 ケイコウオ Finneon", "霓虹鱼 ネオラント Lumineon", "小球飞鱼 タマンタ Mantyke", "雪笠怪 ユキカブリ Snover", "暴雪王 ユキノオー Abomasnow", "玛狃拉 マニューラ Weavile", "自爆磁怪 ジバコイル Magnezone", "大舌舔 ベロベルト Lickilicky", "超甲狂犀 ドサイドン Rhyperior", "巨蔓藤 モジャンボ Tangrowth", "电击魔兽 エレキブル Electivire", "鸭嘴炎兽 ブーバーン Magmortar", "波克基斯* トゲキッス Togekiss", "远古巨蜓 メガヤンマ Yanmega", "叶伊布 リーフィア Leafeon", "冰伊布 グレイシア Glaceon", "天蝎王 グライオン Gliscor", "象牙猪 マンムー Mamoswine", "多边兽Ｚ ポリゴンＺ Porygon-Z", "艾路雷朵 エルレイド Gallade", "大朝北鼻 ダイノーズ Probopass", "黑夜魔灵 ヨノワール Dusknoir", "雪妖女 ユキメノコ Froslass", "洛托姆 ロトム Rotom", "由克希 ユクシー Uxie", "艾姆利多 エムリット Mesprit", "亚克诺姆 アグノム Azelf", "帝牙卢卡 ディアルガ Dialga", "帕路奇亚 パルキア Palkia", "席多蓝恩 ヒードラン Heatran", "雷吉奇卡斯 レジギガス Regigigas", "骑拉帝纳 ギラティナ Giratina", "克雷色利亚 クレセリア Cresselia", "霏欧纳 フィオネ Phione", "玛纳霏 マナフィ Manaphy", "达克莱伊 ダークライ Darkrai", "谢米 シェイミ Shaymin", "阿尔宙斯 アルセウス Arceus", "比克提尼 ビクティニ Victini", "藤藤蛇 ツタージャ Snivy", "青藤蛇 ジャノビー Servine", "君主蛇 ジャローダ Serperior", "暖暖猪 ポカブ Tepig", "炒炒猪 チャオブー Pignite", "炎武王 エンブオー Emboar", "水水獭 ミジュマル Oshawott", "双刃丸 フタチマル Dewott", "大剑鬼 ダイケンキ Samurott", "探探鼠 ミネズミ Patrat", "步哨鼠 ミルホッグ Watchog", "小约克 ヨーテリー Lillipup", "哈约克 ハーデリア Herdier", "长毛狗 ムーランド Stoutland", "扒手猫 チョロネコ Purrloin", "酷豹 レパルダス Liepard", "花椰猴 ヤナップ Pansage", "花椰猿 ヤナッキー Simisage", "爆香猴 バオップ Pansear", "爆香猿 バオッキー Simisear", "冷水猴 ヒヤップ Panpour", "冷水猿 ヒヤッキー Simipour", "食梦梦 ムンナ Munna", "梦梦蚀 ムシャーナ Musharna", "豆豆鸽 マメパト Pidove", "咕咕鸽 ハトーボー Tranquill", "高傲雉鸡 ケンホロウ Unfezant", "斑斑马 シママ Blitzle", "雷电斑马 ゼブライカ Zebstrika", "石丸子 ダンゴロ Roggenrola", "地幔岩 ガントル Boldore", "庞岩怪 ギガイアス Gigalith", "滚滚蝙蝠 コロモリ Woobat", "心蝙蝠 ココロモリ Swoobat", "螺钉地鼠 モグリュー Drilbur", "龙头地鼠 ドリュウズ Excadrill", "差不多娃娃 タブンネ Audino", "搬运小匠 ドッコラー Timburr", "铁骨土人 ドテッコツ Gurdurr", "修建老匠 ローブシン Conkeldurr", "圆蝌蚪 オタマロ Tympole", "蓝蟾蜍 ガマガル Palpitoad", "蟾蜍王 ガマゲロゲ Seismitoad", "投摔鬼 ナゲキ Throh", "打击鬼 ダゲキ Sawk", "虫宝包 クルミル Sewaddle", "宝包茧 クルマユ Swadloon", "保姆虫 ハハコモリ Leavanny", "百足蜈蚣 フシデ Venipede", "车轮球 ホイーガ Whirlipede", "蜈蚣王 ペンドラー Scolipede", "木棉球* モンメン Cottonee", "风妖精* エルフーン Whimsicott", "百合根娃娃 チュリネ Petilil", "裙儿小姐 ドレディア Lilligant", "野蛮鲈鱼 バスラオ Basculin", "黑眼鳄 メグロコ Sandile", "混混鳄 ワルビル Krokorok", "流氓鳄 ワルビアル Krookodile", "火红不倒翁 ダルマッカ Darumaka", "达摩狒狒 ヒヒダルマ Darmanitan", "沙铃仙人掌 マラカッチ Maractus", "石居蟹 イシズマイ Dwebble", "岩殿居蟹 イワパレス Crustle", "滑滑小子 ズルッグ Scraggy", "头巾混混 ズルズキン Scrafty", "象征鸟 シンボラー Sigilyph", "哭哭面具 デスマス Yamask", "死神棺 デスカーン Cofagrigus", "原盖海龟 プロトーガ Tirtouga", "肋骨海龟 アバゴーラ Carracosta", "始祖小鸟 アーケン Archen", "始祖大鸟 アーケオス Archeops", "破破袋 ヤブクロン Trubbish", "灰尘山 ダストダス Garbodor", "索罗亚 ゾロア Zorua", "索罗亚克 ゾロアーク Zoroark", "泡沫栗鼠 チラーミィ Minccino", "奇诺栗鼠 チラチーノ Cinccino", "哥德宝宝 ゴチム Gothita", "哥德小童 ゴチミル Gothorita", "哥德小姐 ゴチルゼル Gothitelle", "单卵细胞球 ユニラン Solosis", "双卵细胞球 ダブラン Duosion", "人造细胞卵 ランクルス Reuniclus", "鸭宝宝 コアルヒー Ducklett", "舞天鹅 スワンナ Swanna", "迷你冰 バニプッチ Vanillite", "多多冰 バニリッチ Vanillish", "双倍多多冰 バイバニラ Vanilluxe", "四季鹿 シキジカ Deerling", "萌芽鹿 メブキジカ Sawsbuck", "电飞鼠 エモンガ Emolga", "盖盖虫 カブルモ Karrablast", "骑士蜗牛 シュバルゴ Escavalier", "哎呀球菇 タマゲタケ Foongus", "败露球菇 モロバレル Amoonguss", "轻飘飘 プルリル Frillish", "胖嘟嘟 ブルンゲル Jellicent", "保姆曼波 ママンボウ Alomomola", "电电虫 バチュル Joltik", "电蜘蛛 デンチュラ Galvantula", "种子铁球 テッシード Ferroseed", "坚果哑铃 ナットレイ Ferrothorn", "齿轮儿 ギアル Klink", "齿轮组 ギギアル Klang", "齿轮怪 ギギギアル Klinklang", "麻麻小鱼 シビシラス Tynamo", "麻麻鳗 シビビール Eelektrik", "麻麻鳗鱼王 シビルドン Eelektross", "小灰怪 リグレー Elgyem", "大宇怪 オーベム Beheeyem", "烛光灵 ヒトモシ Litwick", "灯火幽灵 ランプラー Lampent", "水晶灯火灵 シャンデラ Chandelure", "牙牙 キバゴ Axew", "斧牙龙 オノンド Fraxure", "双斧战龙 オノノクス Haxorus", "喷嚏熊 クマシュン Cubchoo", "冻原熊 ツンベアー Beartic", "几何雪花 フリージオ Cryogonal", "小嘴蜗 チョボマキ Shelmet", "敏捷虫 アギルダー Accelgor", "泥巴鱼 マッギョ Stunfisk", "功夫鼬 コジョフー Mienfoo", "师父鼬 コジョンド Mienshao", "赤面龙 クリムガン Druddigon", "泥偶小人 ゴビット Golett", "泥偶巨人 ゴルーグ Golurk", "驹刀小兵 コマタナ Pawniard", "劈斩司令 キリキザン Bisharp", "爆炸头水牛 バッフロン Bouffalant", "毛头小鹰 ワシボン Rufflet", "勇士雄鹰 ウォーグル Braviary", "秃鹰丫头 バルチャイ Vullaby", "秃鹰娜 バルジーナ Mandibuzz", "熔蚁兽 クイタラン Heatmor", "铁蚁 アイアント Durant", "单首龙 モノズ Deino", "双首暴龙 ジヘッド Zweilous", "三首恶龙 サザンドラ Hydreigon", "燃烧虫 メラルバ Larvesta", "火神蛾 ウルガモス Volcarona", "勾帕路翁 コバルオン Cobalion", "代拉基翁 テラキオン Terrakion", "毕力吉翁 ビリジオン Virizion", "龙卷云 トルネロス Tornadus", "雷电云 ボルトロス Thundurus", "莱希拉姆 レシラム Reshiram", "捷克罗姆 ゼクロム Zekrom", "土地云 ランドロス Landorus", "酋雷姆 キュレム Kyurem", "凯路迪欧 ケルディオ Keldeo", "美洛耶塔 メロエッタ Meloetta", "盖诺赛克特 ゲノセクト Genesect", "哈力栗 ハリマロン Chespin", "胖胖哈力 ハリボーグ Quilladin", "布里卡隆 ブリガロン Chesnaught", "火狐狸 フォッコ Fennekin", "长尾火狐 テールナー Braixen", "妖火红狐 マフォクシー Delphox", "呱呱泡蛙 ケロマツ Froakie", "呱头蛙 ゲコガシラ Frogadier", "甲贺忍蛙 ゲッコウガ Greninja", "掘掘兔 ホルビー Bunnelby", "掘地兔 ホルード Diggersby", "小箭雀 ヤヤコマ Fletchling", "火箭雀 ヒノヤコマ Fletchinder", "烈箭鹰 ファイアロー Talonflame", "粉蝶虫 コフキムシ Scatterbug", "粉蝶蛹 コフーライ Spewpa", "彩粉蝶 ビビヨン Vivillon", "小狮狮 シシコ Litleo", "火炎狮 カエンジシ Pyroar", "花蓓蓓 フラベベ Flabébé", "花叶蒂 フラエッテ Floette", "花洁夫人 フラージェス Florges", "坐骑小羊 メェークル Skiddo", "坐骑山羊 ゴーゴート Gogoat", "顽皮熊猫 ヤンチャム Pancham", "流氓熊猫 ゴロンダ Pangoro", "多丽米亚 トリミアン Furfrou", "妙喵 ニャスパー Espurr", "超能妙喵 ニャオニクス Meowstic", "独剑鞘 ヒトツキ Honedge", "双剑鞘 ニダンギル Doublade", "坚盾剑怪 ギルガルド Aegislash", "粉香香 シュシュプ Spritzee", "芳香精 フレフワン Aromatisse", "绵绵泡芙 ペロッパフ Swirlix", "胖甜妮 ペロリーム Slurpuff", "好啦鱿 マーイーカ Inkay", "乌贼王 カラマネロ Malamar", "龟脚脚 カメテテ Binacle", "龟足巨铠 ガメノデス Barbaracle", "垃垃藻 クズモー Skrelp", "毒藻龙 ドラミドロ Dragalge", "铁臂枪虾 ウデッポウ Clauncher", "钢炮臂虾 ブロスター Clawitzer", "伞电蜥 エリキテル Helioptile", "光电伞蜥 エレザード Heliolisk", "宝宝暴龙 チゴラス Tyrunt", "怪颚龙 ガチゴラス Tyrantrum", "冰雪龙 アマルス Amaura", "冰雪巨龙 アマルルガ Aurorus", "仙子伊布 ニンフィア Sylveon", "摔角鹰人 ルチャブル Hawlucha", "咚咚鼠 デデンネ Dedenne", "小碎钻 メレシー Carbink", "黏黏宝 ヌメラ Goomy", "黏美儿 ヌメイル Sliggoo", "黏美儿  ", "黏美龙 ヌメルゴン Goodra", "钥圈儿 クレッフィ Klefki", "小木灵 ボクレー Phantump", "朽木妖 オーロット Trevenant", "南瓜精 バケッチャ Pumpkaboo", "南瓜怪人 パンプジン Gourgeist", "冰宝 カチコール Bergmite", "冰岩怪 クレベース Avalugg", "嗡蝠 オンバット Noibat", "音波龙 オンバーン Noivern", "哲尔尼亚斯 ゼルネアス Xerneas", "伊裴尔塔尔 イベルタル Yveltal", "基格尔德 ジガルデ Zygarde", "蒂安希 ディアンシー Diancie", "胡帕 フーパ Hoopa", "波尔凯尼恩 ボルケニオン Volcanion", "木木枭 モクロー Rowlet", "投羽枭 フクスロー Dartrix", "狙射树枭 ジュナイパー Decidueye", "火斑喵 ニャビー Litten", "炎热喵 ニャヒート Torracat", "炽焰咆哮虎 ガオガエン Incineroar", "球球海狮 アシマリ Popplio", "花漾海狮 オシャマリ Brionne", "西狮海壬 アシレーヌ Primarina", "小笃儿 ツツケラ Pikipek", "喇叭啄鸟 ケララッパ Trumbeak", "铳嘴大鸟 ドデカバシ Toucannon", "猫鼬少 ヤングース Yungoos", "猫鼬探长 デカグース Gumshoos", "强颚鸡母虫 アゴジムシ Grubbin", "虫电宝 デンヂムシ Charjabug", "锹农炮虫 クワガノン Vikavolt", "好胜蟹 マケンカニ Crabrawler", "好胜毛蟹 ケケンカニ Crabominable", "花舞鸟 オドリドリ Oricorio", "萌虻 アブリー Cutiefly", "蝶结萌虻 アブリボン Ribombee", "岩狗狗 イワンコ Rockruff", "鬃岩狼人 ルガルガン Lycanroc", "弱丁鱼 ヨワシ Wishiwashi", "好坏星 ヒドイデ Mareanie", "超坏星 ドヒドイデ Toxapex", "泥驴仔 ドロバンコ Mudbray", "重泥挽马 バンバドロ Mudsdale", "滴蛛 シズクモ Dewpider", "滴蛛霸 オニシズクモ Araquanid", "伪螳草 カリキリ Fomantis", "兰螳花 ラランテス Lurantis", "睡睡菇 ネマシュ Morelull", "灯罩夜菇 マシェード Shiinotic", "夜盗火蜥 ヤトウモリ Salandit", "焰后蜥 エンニュート Salazzle", "童偶熊 ヌイコグマ Stufful", "穿着熊 キテルグマ Bewear", "甜竹竹 アマカジ Bounsweet", "甜舞妮 アママイコ Steenee", "甜冷美后 アマージョ Tsareena", "花疗环环 キュワワー Comfey", "智挥猩 ヤレユータン Oranguru", "投掷猴 ナゲツケサル Passimian", "胆小虫 コソクムシ Wimpod", "具甲武者 グソクムシャ Golisopod", "沙丘娃 スナバァ Sandygast", "噬沙堡爷 シロデスナ Palossand", "拳海参 ナマコブシ Pyukumuku", "属性：空 タイプ：ヌル Type: Null", "银伴战兽 シルヴァディ Silvally", "小陨星 メテノ Minior", "树枕尾熊 ネッコアラ Komala", "爆焰龟兽 バクガメス Turtonator", "托戈德玛尔 トゲデマル Togedemaru", "谜拟Ｑ ミミッキュ Mimikyu", "磨牙彩皮鱼 ハギギシリ Bruxish", "老翁龙 ジジーロン Drampa", "破破舵轮 ダダリン Dhelmise", "心鳞宝 ジャラコ Jangmo-o", "鳞甲龙 ジャランゴ Hakamo-o", "杖尾鳞甲龙 ジャラランガ Kommo-o", "卡璞・鸣鸣 カプ・コケコ Tapu Koko", "卡璞・蝶蝶 カプ・テテフ Tapu Lele", "卡璞・哞哞 カプ・ブルル Tapu Bulu", "卡璞・鳍鳍 カプ・レヒレ Tapu Fini", "科斯莫古 コスモッグ Cosmog", "科斯莫姆 コスモウム Cosmoem", "索尔迦雷欧 ソルガレオ Solgaleo", "露奈雅拉 ルナアーラ Lunala", "虚吾伊德 ウツロイド Nihilego", "爆肌蚊 マッシブーン Buzzwole", "费洛美螂 フェローチェ Pheromosa", "电束木 デンジュモク Xurkitree", "铁火辉夜 テッカグヤ Celesteela", "纸御剑 カミツルギ Kartana", "恶食大王 アクジキング Guzzlord", "奈克洛兹玛 ネクロズマ Necrozma", "玛机雅娜 マギアナ Magearna", "玛夏多 マーシャドー Marshadow", "毒贝比 ベベノム Poipole", "四颚针龙 アーゴヨン Naganadel", "垒磊石 ツンデツンデ Stakataka", "砰头小丑 ズガドーン Blacephalon", "捷拉奥拉 ゼラオラ Zeraora", "美录坦 メルタン Meltan", "美录梅塔 メルメタル Melmetal", "敲音猴 サルノリ Grookey", "啪咚猴 バチンキー Thwackey", "轰擂金刚猩 ゴリランダー Rillaboom", "炎兔儿 ヒバニー Scorbunny", "腾蹴小将 ラビフット Raboot", "闪焰王牌 エースバーン Cinderace", "泪眼蜥 メッソン Sobble", "变涩蜥 ジメレオン Drizzile", "千面避役 インテレオン Inteleon", "贪心栗鼠 ホシガリス Skwovet", "藏饱栗鼠 ヨクバリス Greedent", "稚山雀 ココガラ Rookidee", "蓝鸦 アオガラス Corvisquire", "钢铠鸦 アーマーガア Corviknight", "索侦虫 サッチムシ Blipbug", "天罩虫 レドームシ Dottler", "以欧路普 イオルブ Orbeetle", "偷儿狐 クスネ Nickit", "狐大盗 フォクスライ Thievul", "幼棉棉 ヒメンカ Gossifleur", "白蓬蓬 ワタシラガ Eldegoss", "毛辫羊 ウールー Wooloo", "毛毛角羊 バイウールー Dubwool", "咬咬龟 カムカメ Chewtle", "暴噬龟 カジリガメ Drednaw", "来电汪 ワンパチ Yamper", "逐电犬 パルスワン Boltund", "小炭仔 タンドン Rolycoly", "大炭车 トロッゴン Carkol", "巨炭山 セキタンザン Coalossal", "啃果虫 カジッチュ Applin", "苹裹龙 アップリュー Flapple", "丰蜜龙 タルップル Appletun", "沙包蛇 スナヘビ Silicobra", "沙螺蟒 サダイジャ Sandaconda", "古月鸟 ウッウ Cramorant", "刺梭鱼 サシカマス Arrokuda", "戽斗尖梭 カマスジョー Barraskewda", "毒电婴 エレズン Toxel", "颤弦蝾螈 ストリンダー Toxtricity", "烧火蚣 ヤクデ Sizzlipede", "焚焰蚣 マルヤクデ Centiskorch", "拳拳蛸 タタッコ Clobbopus", "八爪武师 オトスパス Grapploct", "来悲茶 ヤバチャ Sinistea", "怖思壶 ポットデス Polteageist", "迷布莉姆 ミブリム Hatenna", "提布莉姆 テブリム Hattrem", "布莉姆温 ブリムオン Hatterene", "捣蛋小妖 ベロバー Impidimp", "诈唬魔 ギモー Morgrem", "长毛巨魔 オーロンゲ Grimmsnarl", "堵拦熊 タチフサグマ Obstagoon", "喵头目 ニャイキング Perrserker", "魔灵珊瑚 サニゴーン Cursola", "葱游兵 ネギガナイト Sirfetch'd", "踏冰人偶 バリコオル Mr. Rime", "死神板 デスバーン Runerigus", "小仙奶 マホミル Milcery", "霜奶仙 マホイップ Alcremie", "列阵兵 タイレーツ Falinks", "啪嚓海胆 バチンウニ Pincurchin", "雪吞虫 ユキハミ Snom", "雪绒蛾 モスノウ Frosmoth", "巨石丁 イシヘンジン Stonjourner", "冰砌鹅 コオリッポ Eiscue", "爱管侍 イエッサン Indeedee", "莫鲁贝可 モルペコ Morpeko", "铜象 ゾウドウ Cufant", "大王铜象 ダイオウドウ Copperajah", "雷鸟龙 パッチラゴン Dracozolt", "雷鸟海兽 パッチルドン Arctozolt", "鳃鱼龙 ウオノラゴン Dracovish", "鳃鱼海兽 ウオチルドン Arctovish", "铝钢龙 ジュラルドン Duraludon", "多龙梅西亚 ドラメシヤ Dreepy", "多龙奇 ドロンチ Drakloak", "多龙巴鲁托 ドラパルト Dragapult", "苍响 ザシアン Zacian", "藏玛然特 ザマゼンタ Zamazenta", "无极汰那 ムゲンダイナ Eternatus", "熊徒弟 ダクマ Kubfu", "武道熊师 ウーラオス Urshifu", "萨戮德 ザルード Zarude", "雷吉艾勒奇 レジエレキ Regieleki", "雷吉铎拉戈 レジドラゴ Regidrago", "雪暴马 ブリザポス Glastrier", "灵幽马 レイスポス Spectrier", "蕾冠王 バドレックス Calyrex", "诡角鹿 アヤシシ Wyrdeer", "劈斧螳螂 バサギリ Kleavor", "月月熊 ガチグマ Ursaluna", "幽尾玄鱼 イダイトウ Basculegion", "大狃拉 オオニューラ Sneasler", "万针鱼 ハリーマン Overqwil", "眷恋云 ラブトロス Enamorus");
    private final static List<String> fiveStars_2 = Arrays.asList("鸣依（2019冬季）&信使鸟", "志米（2019冬季）&章鱼桶", "阿渡&快龙", "竹兰&烈咬陆鲨", "大吾&巨金怪", "美极套装赤红&喷火龙", "琴音（2020夏季）&胖丁", "大吾（2020夏季）&穿山王（阿罗拉的样子）", "美极套装竹兰&杖尾鳞甲龙", "美极套装青绿&水箭龟", "美极套装叶子&妙蛙花", "斗也（2020秋季）&大狼犬", "阿塞罗拉（2020秋季）&谜拟Q", "阿戴克&火神蛾", "小优&苍响（剑之王）", "风露（2020冬季）&波克基斯", "莉佳（2020冬季）&花疗环环", "N&捷克罗姆", "阿渡（2021新春）&暴鲤龙", "莉莉艾（2021新春）&蝶结萌虻", "莎莉娜（2021春季）&风妖精", "小光（2021春季）&霜奶仙", "弗拉达利&伊裴尔塔尔", "丹帝&喷火龙", "玛俐&莫鲁贝可", "艾莉丝（冠军）&三首恶龙", "卡露妮&沙奈朵", "小遥(2021春季)&长耳兔", "亚堤（2021春季）&波克比", "奇巴纳&铝钢龙", "赤焰松&固拉多", "水梧桐&盖欧卡", "北尚&龙头地鼠", "南厦&始祖大鸟", "小优（2021夏季）&千面避役", "玛俐（2021夏季）&长毛巨魔", "鎯琊&胡帕", "美极套装丹帝&无极汰那", "莉莉艾（2021周年庆）&露奈雅拉", "N（2021周年庆）&莱希拉姆", "大吾（2021周年庆）&黑色烈空坐", "赤红&卡比兽", "青绿（关都）&化石翼龙", "松叶（2021秋季）&诅咒娃娃", "嘉德丽雅（2021秋季）&勾魂眼", "卡露妮（铠甲）&凯路迪欧（觉悟的样子）", "斗子（礼服）&蒂安希", "古兹马（铠甲）&爆肌蚊", "阿蜜（礼服）&铁火辉夜", "美极套装露莎米奈&奈克洛兹玛（黄昏之鬃）", "小菊儿（合众）&电飞鼠", "丹帝（2021冬季）&蕾冠王（骑白马的样子）", "露璃娜（2021冬季）&冰砌鹅（结冻头）", "美极套装竹兰（异装）&骑拉帝纳（别种形态）", "电次（2022新春）&电击魔兽", "娜姿（2022新春）&铃铛响", "明辉&帝牙卢卡", "玛俐（2022春季）&大嘴娃", "彩豆（2022春季）&双倍多多冰", "美极套装赤日&达克莱伊", "美极套装小光&克雷色利亚", "小遥（2022周年庆）&拉帝亚斯", "风露（2022周年庆）&龙卷云", "奇巴纳（2022周年庆）&沙漠蜻蜓", "美极套装莎莉娜&基格尔德（50%形态）", "莉莉艾（洋装）&怖思壶", "索妮亚（礼服）&甜冷美后（*）", "北尚（礼服）&敏捷虫", "南厦（礼服）&骑士蜗牛", "美极套装松叶&凤王（*）", "美极套装坂木&尼多王", "美极套装阿响&洛奇亚", "美极套装琴音&时拉比", "美极套装克丽丝&水君", "彼特&布莉姆温", "N（2022夏季）&索罗亚克", "斗子（2022夏季）&八爪武师（*）", "小优（修行套装）&武道熊师（连击流）", "美极套装可尔妮&玛夏多");
    private final static List<String> fiveStars_10 = Arrays.asList("克丽丝&小锯鳄", "梨花&黑鲁加", "小悠&木守宫", "芙蓉&彷徨夜灵", "丽姿&鬃岩狼人（黑夜的样子）", "青绿&大比鸟", "琴音&菊草叶", "斗也&水水濑", "斗子&暖暖猪", "小菊儿&雷电斑马", "嘉德丽雅&人造细胞卵", "波妮&冰鬼护", "越橘&酷豹", "阿响&火球鼠", "叶子&伊布", "库库伊&鬃岩狼人（白昼的样子）", "美极套装小菊儿&洛托姆", "娜姿&胡地", "梅丽莎&梦妖魔", "古兹马&具甲武者", "布尔美丽&焰后蜥", "希特隆&光电伞蜥", "米可利&美纳斯", "满充&艾路雷朵", "阿蜜&大钢蛇", "小光&草苗龟", "美极套装越橘&巨牙鲨", "亚堤&保姆虫", "莎莉娜&火狐狸", "松叶&随风球", "露莎米奈&费洛美螂", "格拉吉欧&银伴战兽", "莉莉艾&皮皮", "电次&伦琴猫", "琉琪亚&七夕青鸟", "共平&勇士雄鹰", "白露&梦梦蚀", "小遥&水跃鱼", "玛奥&甜冷美后", "聂梓&堵拦熊", "美月&木木枭", "小阳&球球海狮", "露璃娜&暴噬龟", "彩豆&葱游兵", "水莲&滴蛛霸", "卡奇&嘎啦嘎啦（阿罗拉的样子）", "哈拉&好胜毛蟹", "皇家蒙面人&炽焰咆哮虎", "阿速&大王燕", "花月&阿勃梭鲁", "美极套装小霞&水伊布", "美极套装莉佳&叶伊布", "欧尼奥&耿鬼", "石兰&姆克鹰", "越橘（便装）&劈斩司令", "索妮亚&来电汪", "阿柳&蜂女王", "菊野&河马兽", "悟松&麒麟奇");
    private final static List<String> fourStars_20 = Arrays.asList("棘儿&月亮伊布","螺伯&乌鸦头头","夏伯&小火马", "科拿&拉普拉斯", "希巴&怪力", "菊子&耿鬼", "小茜&大奶罐", "一树&天然鸟", "源治&暴飞龙", "达拉&凯罗斯", "菜种&罗丝蕾朵", "捩木&青铜钟", "霍米加&车轮球", "婉龙&水晶灯火灵", "连武&修建老匠", "查克洛&冰雪龙", "志米&钢炮臂虾", "雁铠&坚盾剑怪", "马玛内&托戈德玛尔", "卡希丽&铳嘴大鸟", "小蓟&饭匙蛇");
    private final static List<String> threeStars_68 = Arrays.asList("马志士&霹雳电球", "阿笔&大针蜂", "阿杏&阿利多斯", "杜鹃&朝北鼻", "藤树&幕下力士", "娜琪&大嘴鸥", "小枫&太阳岩", "小南&月石", "瓢太&头盖龙", "阿李&玛沙那", "吉宪&浮潜鼬", "芽米&幸福蛋", "米依&风速狗", "菊老大&蓝蟾蜍", "哈奇库&几何雪花", "西子伊&肋骨海龟", "福爷&口呆花", "得抚&冰岩怪", "茉莉&布鲁皇");
}
