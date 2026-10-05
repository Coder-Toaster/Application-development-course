# Viikkoharjoitus 6 – Kuvat listassa, tekstihaku ja navigointi

## Toteutus

Lähtöprojektissa oli yksi opiskelijaprofiili, mutta ei edellisen viikon listaa tai Boolean-suodatinta. Toteutusta varten profiilista tehtiin `Profile`-dataluokka ja muodostettiin viiden profiilin Kotlin-lista. Patrik Verhon alkuperäiset tiedot säilytettiin; neljä muuta profiilia ovat kuvitteellista harjoitusdataa. Boolean-ominaisuus `isAvailableForProjects` tarkoittaa, että opiskelija on käytettävissä projekteihin. Myös Patrikin käytettävyys on harjoitusta varten asetettu esimerkkiarvo.

| Tehtävän vaatimus | Toteutus |
| --- | --- |
| Kuva listan alkioissa | `ProfileCard`, `Image`, `painterResource`, `fillMaxWidth`, `height`, `padding` ja `ContentScale.Fit` |
| Tekstihaku heti kirjoitettaessa | `OutlinedTextField` päivittää `searchText`-tilan jokaisella muutoksella |
| Haku säilyy näytön kääntämisessä | `rememberSaveable` hakutekstille ja Boolean-suodattimelle |
| Haku ja Boolean-suodatin yhdessä | `filterProfiles` yhdistää ehdot `&&`-operaattorilla |
| Kirjainkoko ei vaikuta | `contains(query, ignoreCase = true)` |
| Tyhjä hakukenttä | Näyttää kaikki käytettävyysrajauksen täyttävät profiilit |
| Ei hakutuloksia | Listassa näkyy ohje hakusanan tai suodattimen muuttamiseen |
| Klikattava kortti | Material 3:n `ElevatedCard(onClick = onClick)` ja callback |
| Detaljinäkymä | `ProfileDetailScreen(profile, onBackClick)`, `Scaffold`, `TopAppBar`, kuva, nimi, rooli, kuvaus, oppilaitos, suuntautuminen ja kiinnostukset |
| Boolean-arvon näkyvä vaikutus | Käytettävyysteksti sekä värillinen tila, true-arvolla myös kuvake |
| Takaisin-painikkeen saavutettavuus | `R.string.navigate_back` englanniksi ja suomeksi |
| Kaksi navigointikohdetta | Tyypitetyt `ProfileListRoute` ja `ProfileDetailRoute` |
| Reitillä vain id | `ProfileDetailRoute(profileId: Int)`; oikea profiili etsitään alkuperäisestä listasta |
| Takaisin listaan | Yläpalkki kutsuu `navigateUp()`; järjestelmän takaisin-toimintoa käsittelee `NavController` |
| Puuttuva id | `ProfileNotFoundScreen` näyttää virheilmoituksen ja takaisin-painikkeen |

## Käyttö

1. Avaa tämä projekti Android Studiossa ja suorita Gradle Sync. Valitse `app` ja käynnistä sovellus Run-painikkeella.
2. Aloitusnäkymä näyttää kaikki viisi profiilia. Vieritä listaa nähdäksesi loput kortit.
3. Kirjoita hakukenttään esimerkiksi `android`. Haku löytää Ainon ja Eliaksen kiinnostusten perusteella. Haku kohdistuu myös nimeen, rooliin, oppilaitokseen ja suuntautumiseen.
4. Kytke käytettävyysrajaus päälle. `android`-haulla jäljelle jää vain Aino, koska Eliaksen Boolean-arvo on false.
5. Tyhjennä hakukenttä sen X-painikkeella. Jos käytettävyysrajaus on päällä, näkyviin tulevat Patrik, Aino ja Sara. Poistamalla myös rajauksen saat kaikki viisi näkyviin.
6. Paina profiilikorttia avataksesi valitun henkilön tiedot. Vieritä tarvittaessa detaljinäkymää.
7. Palaa yläpalkin nuolella tai laitteen takaisin-toiminnolla. Hakuteksti, suodatin ja listan vieritystila palautuvat.
8. Kokeile hakua `zzzz`, jolloin näkyy tyhjän tuloksen viesti. Käännä näyttöä haku ja suodatin päällä ja tarkista, että molemmat säilyvät.

## Miten koodi toimii

`Profiles.kt` sisältää dataluokan, `sampleProfiles`-listan, hakusuodatuksen ja id-haun. `ProfileScreens.kt` sisältää listan, kortin, detaljinäkymän ja puuttuvan profiilin näkymän. `ProfileRoutes.kt` määrittelee reitit. `MainActivity.kt` käynnistää sovelluksen ja kokoaa navigoinnin `ProfileApp`-funktiossa.

Hakukentän ja suodattimen arvot ovat Compose-tilaa. Tilamuutos käynnistää uudelleenkoostamisen, jolloin näkyvä lista lasketaan uudelleen. `rememberSaveable` säilyttää arvot Androidin tallennettavassa käyttöliittymätilassa esimerkiksi näytön kääntämisen yli; se ei ole tietokanta tai pysyvä käyttäjäasetusten tallennus.

```kotlin
var searchText by rememberSaveable { mutableStateOf("") }
var availableOnly by rememberSaveable { mutableStateOf(false) }
val visibleProfiles = filterProfiles(profiles, searchText, availableOnly)

OutlinedTextField(
    value = searchText,
    onValueChange = { searchText = it },
    label = { Text(stringResource(R.string.search_label)) },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
)
```

Suodatuksessa ei muuteta alkuperäistä listaa eikä hakutulosta tallenneta omaan tilamuuttujaan:

```kotlin
val query = searchText.trim()
return profiles.filter { profile ->
    val matchesAvailability = !availableOnly || profile.isAvailableForProjects
    val matchesSearch = query.isEmpty() ||
        profile.name.contains(query, ignoreCase = true) ||
        profile.role.contains(query, ignoreCase = true) ||
        profile.school.contains(query, ignoreCase = true) ||
        profile.major.contains(query, ignoreCase = true) ||
        profile.interests.contains(query, ignoreCase = true)
    matchesAvailability && matchesSearch
}
```

Kortin klikkauskäsittely:

```kotlin
ProfileCard(
    profile = profile,
    onClick = {
        focusManager.clearFocus()
        onProfileClick(profile.id)
    }
)

@Composable
fun ProfileCard(profile: Profile, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        // Kuva ja profiilin tiivistelmä.
    }
}
```

Navigointireitit:

```kotlin
@Serializable
data object ProfileListRoute

@Serializable
data class ProfileDetailRoute(val profileId: Int)
```

NavHost-rakenne:

```kotlin
val navController = rememberNavController()
NavHost(navController = navController, startDestination = ProfileListRoute) {
    composable<ProfileListRoute> {
        ProfileListScreen(
            profiles = sampleProfiles,
            onProfileClick = { id ->
                navController.navigate(ProfileDetailRoute(id)) {
                    launchSingleTop = true
                }
            }
        )
    }
    composable<ProfileDetailRoute> { entry ->
        val route = entry.toRoute<ProfileDetailRoute>()
        val profile = findProfileById(sampleProfiles, route.profileId)
        val onBackClick: () -> Unit = { navController.navigateUp() }
        if (profile != null) {
            ProfileDetailScreen(profile = profile, onBackClick = onBackClick)
        } else {
            ProfileNotFoundScreen(onBackClick = onBackClick)
        }
    }
}
```

## Gradle-muutokset

- `gradle/libs.versions.toml`: lisättiin Navigation Compose `2.10.2`, Kotlin Serialization JSON `1.7.3` ja serialization-pluginin alias. Plugin käyttää projektin olemassa olevaa Kotlin-versiota `2.2.10`.
- Projektin `build.gradle.kts`: lisättiin `alias(libs.plugins.kotlin.serialization) apply false`.
- `app/build.gradle.kts`: otettiin serialization-plugin käyttöön ja lisättiin `implementation(libs.androidx.navigation.compose)` sekä `implementation(libs.kotlinx.serialization.json)`. Nykyinen ikoniriippuvuus siirrettiin käyttämään jo olemassa olevaa version catalog -aliasta.
- Navigation-riippuvuus on `androidx.navigation:navigation-compose:2.10.2`.
- Serialization-riippuvuus on `org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3` ja plugin `org.jetbrains.kotlin.plugin.serialization:2.2.10`. Plugin tuottaa reittien tarvitsemat serializerit `@Serializable`-määrityksistä. Tavallinen Androidin Kotlin-tuki tulee nykyisessä projektissa AGP:n sisäänrakennetusta Kotlin-tuesta.
- Gradle-konfigurointi, riippuvuuksien ratkaisu ja sovelluksen debug-käännös onnistuivat komentoriviltä. Android Studion loki vahvistaa myös onnistuneen Gradle Syncin 5.10.2026 klo 06.48 (`onSuccess`, `onImportFinished`, `Gradle sync finished`).

Tyypitetty rakenne seuraa [Androidin virallista Navigation Compose -ohjetta](https://developer.android.com/guide/navigation/design/type-safety). Käytetty Navigation-versio on dokumentoitu [julkaisutiedoissa](https://developer.android.com/jetpack/androidx/releases/navigation).

## Kuvan lähde

Kaikissa korteissa ja detaljinäkymissä käytetään projektissa jo ollutta kissakuvitusta, `cat_5968876_960_720`. Se on koristeellinen yhteinen kuva, joten `contentDescription = null`. Se ei esitä profiilin henkilöä.

Tekijä: **nekomachines**. [Alkuperäinen kuva Pixabay-palvelussa](https://pixabay.com/illustrations/cat-kitten-pet-brown-cat-kitty-5968876/). Kuvan sivu ilmoittaa käyttöoikeudeksi Pixabay Content License -lisenssin; [lisenssin yhteenveto](https://pixabay.com/service/license-summary/) sallii maksuttoman käytön lisenssin ehtojen mukaisesti.

## Lyhyet vastaukset – luonnos

Alla olevat vastaukset ovat AI:n laatimia opiskelun tueksi. Muokkaa ne omiksi sanoiksesi ja varmista, että osaat selittää ne oman sovelluksesi kautta.

1. **Miksi hakuteksti tarvitsee tilamuuttujan?** Hakuteksti muuttuu käyttäjän kirjoittaessa. Compose-tilamuuttujan muuttuminen päivittää hakukentän ja käynnistää listan uudelleenkoostamisen.
2. **Miksi suodatettua listaa ei tarvitse tallentaa erilliseen tilaan?** Se voidaan laskea alkuperäisestä listasta, hakutekstistä ja Boolean-suodattimesta. Erillinen tila olisi saman tiedon toinen kopio ja voisi jäädä vanhentuneeksi.
3. **Mitä eroa on alkuperäisellä oliolistalla ja näkyvällä listalla?** Alkuperäinen lista sisältää kaikki viisi profiilia. Näkyvä lista sisältää vain tämänhetkiset hakuehdot täyttävät profiilit, mutta suodatus ei poista alkioita alkuperäisestä listasta.
4. **Miksi jokaisella oliolla tulee olla yksilöllinen id?** Id tunnistaa profiilin riippumatta nimestä tai listan järjestyksestä. Sitä käytetään detaljinäkymän hakuun ja LazyColumnin vakaana avaimena.
5. **Miksi reitillä välitetään vain tunniste eikä koko oliota?** Tunniste pitää reitin pienenä ja yksinkertaisena. Detaljinäkymä hakee profiilin samasta alkuperäisestä listasta, jolloin tietoja ei kopioida navigointiargumentteihin.
6. **Mitä navigointipino tarkoittaa?** Se on avattujen navigointikohteiden järjestetty pino. Kun listasta avataan detaljinäkymä, detalji lisätään listan päälle ja takaisin-toiminto voi poistaa sen.
7. **Mitä navigateUp tai vastaava takaisin-toiminto tekee?** Tässä sovelluksessa `navigateUp()` poistaa detaljikohteen pinon päältä ja näyttää aiemman listakohteen. Listan tallennettu haku ja suodatin palautuvat.
8. **Miksi kortille annetaan onClick-callback?** Kortti ilmoittaa klikkauksesta ylemmälle komponentille. Silloin se ei tarvitse NavControlleria ja sitä voidaan käyttää ja esikatsella myös muissa yhteyksissä.
9. **Mitä tapahtuu, jos detaljireitin tunnisteella ei löydy oliota?** `firstOrNull` palauttaa null-arvon. Sovellus näyttää `ProfileNotFoundScreen`-näkymän ja takaisin-painikkeen kaatumisen sijaan.
10. **Mitä agentti muutti riippuvuuksiin?** Codex lisäsi Navigation Compose- ja Kotlin Serialization JSON -riippuvuudet version catalogiin sekä serialization-pluginin projektin ja app-moduulin Gradle-määrityksiin. Pluginin Kotlin-versio pidettiin samana kuin projektin nykyinen Kotlin-versio.

## Testaus ja palautus

Tarkistukset 5.10.2026:

- `:app:assembleDebug`: onnistui. APK löytyy `app/build/outputs/apk/debug/app-debug.apk`.
- `:app:testDebugUnitTest`: 10 testiä läpäisi (7 hakua/id-hakua koskevaa, 2 reittien serialisointia koskevaa ja projektin alkuperäinen esimerkkitesti).
- `:app:lintDebug`: 0 virhettä; 18 varoitusta, jotka liittyvät esimerkiksi riippuvuuksien uusiin versioihin ja aiempiin käyttämättömiin resursseihin.
- `:app:assembleDebugAndroidTest`: onnistui. `ProfileFlowTest` sisältää tarkistukset yhdistetyille suodattimille, tyhjälle hakutulokselle, molemmille takaisin-toiminnoille ja Activityn uudelleenluonnille.
- Android Studion Gradle Sync: onnistui (IDE:n lokista vahvistettu).
- Laitteella/emulaattorissa suoritettavat UI-testit ja kuvakaappaukset: odottavat toimivaa laitetta. API 37 -emulaattori on jäänyt offline-tilaan jo ennen sovelluksen asennusta; Android Studio ilmoitti `Emulator failed to connect within 5 minutes`. Sovelluksen käynnistymistä ei tämän perusteella voi vielä vahvistaa.

Komentoriviltä tarkistukset voi suorittaa projektin juuressa:

```bash
JAVA_HOME=/home/toaster/apps/android-studio/jbr bash gradlew \
  :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest

# Kun laite tai emulaattori on käynnissä:
JAVA_HOME=/home/toaster/apps/android-studio/jbr bash gradlew :app:connectedDebugAndroidTest
```

Palautukseen tarvitaan listan, tekstillä suodatetun listan ja detaljinäkymän kuvakaappaukset. Hakutoiminnon koodikatkelma on yllä. Reiteistä/NavHostista ja kortin klikkauskäsittelystä ota lisäksi tehtävänannon pyytämät kuvakaappaukset Android Studion editorista.

## AI-seloste – luonnos

Codexille annettiin nykyinen projekti ja viikon 6 tehtävänanto. Codex toteutti puuttuvan profiililistan, kuvat, tallennettavan hakutilan ja Boolean-suodattimen, tyypitetyn navigoinnin, detaljinäkymän ja puuttuvan profiilin käsittelyn. Se lisäsi tarvittavat Gradle-riippuvuudet ja testit sekä laati tämän selosteen ja vastausluonnokset. Testauksesta ilmoitetaan vain suoritetut tarkistukset; opiskelijan oma koodin tarkastus, ymmärtäminen ja palautuksen viimeistely kirjataan vasta niiden tekemisen jälkeen.
