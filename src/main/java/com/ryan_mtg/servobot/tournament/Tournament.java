package com.ryan_mtg.servobot.tournament;

import com.ryan_mtg.servobot.channelfireball.mfo.MfoInformer;
import com.ryan_mtg.servobot.utility.Time;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tournament {
    private static final int LEADERS = 16;

    @Getter
    private String name;

    @Getter @Setter
    private String nickName;

    @Getter @Setter
    private int round;

    @Getter @Setter
    private String pairingsUrl;

    @Getter @Setter
    private String standingsUrl;

    @Getter @Setter
    private String decklistUrl;

    @Getter @Setter
    private Standings standings;

    @Getter @Setter
    private Pairings pairings;

    @Getter @Setter
    private Instant startTime;

    @Getter @Setter
    private TournamentType type;

    private MfoInformer mfoInformer;
    private Map<Player, DecklistDescription> decklistMap;
    private int id;

    public Tournament(final MfoInformer informer, final String name, final int id) {
        this.mfoInformer = informer;
        this.name = name;
        this.id = id;
    }

    private static final Set<Player> CARE_ABOUTS = new HashSet<>(Arrays.asList(
        new Player("MZBlazer#72009", null, null, null, null, "MTGMilan"),
        new Player("Filipa#15754", null, null, null, "filipacarola", "filipamtg"),
        new Player("Booradley95#84650", null, null, null, null, "bradleyyoo_mtg"),
        new Player("Conanhawk#53621", null, "Eric Hawkins", null, "conanhawk", "conanhawk"),
        new Player("h0lydiva#65001", null, "Daniela Diaz", null, "h0lydiva", "h0lyDiva"),
        new Player("themightylinguine#94385", null, "Carolyn Kavanagh", "Moosers",
                "themightylinguine", "mightylinguine"),

        new Player("Ben_Stark#20548", null, "Ben Stark", "BenS", "bens_mtg", "BenS_MTG"),
        new Player("Brad_Nelson#99373", null, "Brad Nelson", "Bard Nelson", "FFfreakmtg", "fffreakmtg"),
        new Player("Dylan_Nollen#24794", null, "Dylan Nollen", "Ni_Hao_DyLan", "ni_hao_Dylan", "Ni_Hao_DyLan"),
        new Player("Eric_Froehlich#19362", null, "Eric Froehlich", "EFro", "efropoker", "efropoker"),
        new Player("Harlan_Firer#24130", null, "Harlan Firer", "Harlan Firer", null, "HarlanFirer"),
        new Player("Ben_Honaker#85200", null, "Ben Honaker", "Ben Honaker", "imissedmyq", "IMissedMyQ"),
        new Player("Jason_Fleurant#38295", null, "Jason Fleurant", "Jason Fleurant", "jasonfleurant", "JasonFleurant"),
        new Player("Jeremy_Dezani#79986", null, "Jeremy Dezani", "Jeremy Dezani", "jeremdez", "JDezani"),
        new Player("Jessica_Estephan#45539", null, "Jessica Estephan", null, "jesstephan", "jesstephan"),
        new Player("Gal_Schlesinger#42277", null, "Gal Schlesinger", "Yamakiller", "yamakiller", "yamakiller_MTG"),
        new Player("Giana_Kaplan#05304", null, "Giana Kaplan", "Bloody", "Bloody", "Bloody"),
        new Player("Logan_Nettles#78747", null, "Logan Nettles", "Jaberwocki", "jaberwocki", "Jaberwocki"),
        new Player("Marcela_Almeida#18118", null, "Marcela Almeida", null, null, "LindaMahzinha"),
        new Player("Seth_Manfield#52341", null, "Seth Manfield", "Seth Manfield", "sethmanfieldmtg" , "SethManfield"),
        new Player("ShiTian_Lee#26042", null, "Lee Shi Tian", "Lee Shi Tian", "leearson", "leearson"),
        new Player("Teruya_Kakumae#36412", null, "Teruya Kakumae", "Teruya Kakumae", null,"fushiginokunin5"),
        new Player("Tom_Ross#34256", null, "Tom Ross", "Tom 'the Boss' Ross", "BossMTG", "Boss_MTG"),
        new Player("Yoshihiko_Ikawa#15013", null, "Yoshihiko Ikawa", "Yoshihiko Ikawa", "WanderingOnes_", "WanderingOnes"),

        new Player("Aaron_Barich#54175", null, "Aaron Barich", "Aaron_Barich", "RuneclawBarich", "RuneclawBarich"),
        new Player("Alexander_Hayne#41179", null, "Alexander_Hayne", "Insayne Hayne", "insaynehayne", "InsayneHayne"),
        new Player("Allen_Wu#39459", null, "Allen Wu", "Allen Wu", null, "nalkpas"),
        new Player("Andrea_Mengucci#27316", null, "Andrea Mengucci", "Mengu", "andreamengucci", "Mengu09"),
        new Player("Ashley_MunozPreyeses#64645", null, "Ashley Munoz Preyeses", "Ashlizzlle", "ashlizzlle", "F2K_Ashlizzlle"),
        new Player("Carlos_Romao#28636", null, "Carlos Romao", "Carlos Romao", "cadu_romao", "Jabsmtg"),
        new Player("Christopher_Kvartek#57153", null, "Christopher Kvartek", "Chris Kvartek", "bavartech", "Kavartech"),
        new Player("Corey_Burkhart#92780", null, "Corey Burkhart", "Corey Burkhart", null, "Corey_Burkhart"),
        new Player("Dylan_Donegan#98054", null, "Dylan Donegan", "Dylan Donegan", null, "DylanD_MTG"),
        new Player("Eric_Severson#37783", null, "Eric Severson", null, null, "EricESeverson"),
        new Player("Gabriel_Nassif#64992", null, "Gabriel_Nassif", "Yellow Hat", "yellowhat", "gabnassif"),
        new Player("Gerard_Fabiano#41670", null, "Gerard Fabiano", null, null, null),
        new Player("Jack_Kiefer#39736", null, "Jack Kiefer", "Jack Kiefer", null, "JackBKiefer"),
        new Player("Jelger_Wiegersma#50276", null, "Jelger Wiegersma", "Jelger_Wiegersma", null, "WJelger"),
        new Player("John_Rolf#23321", null, "John Rolf", "John Rolf", null, "JRolfMTG"),
        new Player("Kenji_Egashira#32540", null, "Kenji Egashira", "Numot the Nummy", "NumotTheNummy", "NumotTheNummy"),
        new Player("Lucas_EsperBerthoud#73368", null, "Lucas Esper Berthoud", "Lucas Esper Berthoud", "lucas_esper_berthoud", "bertuuuu"),
        new Player("Luis_Salvatto#16501", null, "Luis Salvatto", "LSV (Luis Salvatto)", "luissalvatto", "LuisSalvatto"),
        new Player("Marcio_Carvalho#28667", null, "Marcio Carvalho", "Marcio Carvalho", "kbol_", "KbolMagic"),
        new Player("Mason_Clark#60209", null, "Mason Clark", "Mason Clark", "themasonclark", "masoneclark"),
        new Player("Matias_Leveratto#57297", null, "Matias Leveratto", "Matias Leveratto", "levunga21", "levunga"),
        new Player("Michael_Jacob#11358", null, "Michael_Jacob", "Darkest Mage", "darkest_mage", "Darkest_MAJ"),
        new Player("NoAh_Ma#86867", null, "No Ah Ma", null, null, null),
        new Player("Pascal_Vieren#33243", null, "Pascal Vieren", "Pascal Vieren", null, "VierenPascal"),
        new Player("Patrick_Chapin#86733", null, "Patrick Chapin", "The Innovator", null, "thepchapin"),
        new Player("Peter_Ward#83518", null, "Pete Ward", "Killablastlol", "killablastlol", "Killablastlol"),
        new Player("Reid_Duke#39683", null, "Reid Duke", "Reid Duke", "reiderrabbit", "ReidDuke"),
        new Player("Sebastian_Pozzo#95156", null, "Sebastian Pozzo", "Sebastian Pozzo", "sebastianpozzo", "sebastianpozzo"),
        new Player("Shahar_Shenhar#18991", null, "Shahar Shenhar", "Shahar Shenhar", "shahar_shenhar", "shaharshenhar"),
        new Player("Shota_Yasooka#52724", null, "Shota Yasooka", "Shota Yasooka", "yaya3_", "yaya3_"),
        new Player("Simon_Nielsen#67571", null, "Simon Nielsen", "Simon Nielsen", "redbuttontie", "MrChecklistcard"),
        new Player("Steven_Rubin#44697", null, "Steve Rubin", "Steve Rubin", null, "RubinZoo"),
        new Player("Theo_Moutier#82420", null, "Theo Moutier", null, null, null),
        new Player("Thiago_Saporito#93539", null, "Thiago Saporito", "Thiago Saporito", null, "bolov0"),
        new Player("Thoralf_Severin#83661", null, "Thoralf Severin", "Toffel", null, "ToffelMTG"),
        new Player("Tommi_Hovi#24660", null, "Tommi Hovi", null, null, null),
        new Player("William_Jensen#25008", null, "William Jensen", "Huey", "hueywj", "HueyJensen")
        //new Player("", null, "", null, null, null),
    ));

    public List<PlayerStanding> getPlayers() {
        List<PlayerStanding> players = new ArrayList<>();
        PlayerSet playerSet = standings.getPlayerSet();
        mergeCareAbouts(playerSet, CARE_ABOUTS);

        Record leaderRecord = getLeaderRecord();
        updateDecklistMap();

        for (Player player : playerSet) {
            Player opponent = pairings.getOpponent(player);
            players.add(new PlayerStanding(player, standings.getRank(player), isWatchable(player),
                isLeader(leaderRecord, player), standings.getRecord(player), decklistMap.get(player), opponent,
                decklistMap.get(opponent)));
        }

        Collections.sort(players);
        return players;
    }

    public List<ArchetypeDescription> getMetagameBreakdown() {
        Map<String, Integer> archetypeCount = new HashMap<>();
        updateDecklistMap();
        for (DecklistDescription decklistDescription : decklistMap.values()) {
            int previousCount = archetypeCount.computeIfAbsent(decklistDescription.getName(), name -> 0);
            archetypeCount.put(decklistDescription.getName(), previousCount + 1);
        }

        List<ArchetypeDescription> archetypeDescriptions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : archetypeCount.entrySet()) {
            archetypeDescriptions.add(new ArchetypeDescription(entry.getKey(), entry.getValue(),
                    (double)entry.getValue() / decklistMap.size()));
        }

        Collections.sort(archetypeDescriptions);
        return archetypeDescriptions;
    }

    public boolean hasStarted() {
        return Instant.now().compareTo(startTime) >= 0;
    }

    public String getTimeUntilStart() {
        return Time.toReadableString(Duration.between(Instant.now(), startTime));
    }

    private void mergeCareAbouts(final PlayerSet playerSet, final Set<Player> careAbouts) {
        for (Player careAboutPlayer : careAbouts) {
            if (playerSet.findByArenaName(careAboutPlayer.getArenaName()) != null) {
                playerSet.merge(careAboutPlayer);
            }
        }
    }

    private boolean isLeader(final Record leaderRecord, final Player player) {
        if (leaderRecord == null) {
            return false;
        }

        Record playerRecord = standings.getRecord(player);
        return playerRecord.compareTo(leaderRecord) >= 0;
    }

    private boolean isWatchable(final Player player) {
        for (Player careAboutPlayer : CARE_ABOUTS) {
            if (player.getArenaName().equals(careAboutPlayer.getArenaName())) {
                return true;
            }
        }
        return false;
    }

    private Record getLeaderRecord() {
        List<RecordCount> recordCounts = standings.getRecordCounts(4);
        if (recordCounts.isEmpty() || recordCounts.get(0).getCount() > LEADERS) {
            return null;
        }

        int leaders = 0;
        int index = 0;
        Record bestRecord = recordCounts.get(0).getRecord();
        while (index < recordCounts.size() && leaders + recordCounts.get(index).getCount() <= LEADERS) {
            leaders += recordCounts.get(index).getCount();
            bestRecord = recordCounts.get(index).getRecord();
            index++;
        }

        return bestRecord;
    }

    private void updateDecklistMap() {
        if (decklistMap == null) {
            decklistMap = mfoInformer.parseDecklistsFor(standings.getPlayerSet(), id);
        }
    }
}
