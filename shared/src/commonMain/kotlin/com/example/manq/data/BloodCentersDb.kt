package com.example.manq.data

/** Довідник центрів крові України. Вшитий у додаток, працює без інтернету. */
object BloodCentersDb {

    val oblasts: List<Oblast> = listOf(
        Oblast(
            name = "Вінницька область",
            cities = listOf(
                City(
                    name = "Вінниця",
                    centers = listOf(
                        Center("vinnytsia", "Вінницький центр служби крові", "+38 (067) 492-00-34", "vrcb.vin@gmail.com", "donorvn", "-", "вулиця Пирогова, 48, Вінниця", true, true, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Волинська область",
            cities = listOf(
                City(
                    name = "Володимир-Волинський",
                    centers = listOf(
                        Center("volodymyr-volynskyi", "Володимир-Волинська міська станція переливання крові", "+38 (033) 422-18-93", "-", "-", "-", "вулиця Северина Наливайка, 18, Володимир", true, false, true),
                    ),
                ),
                City(
                    name = "Луцьк",
                    centers = listOf(
                        Center("lutsk", "Волинський обласний центр крові", "+38 (033) 275-75-24", "-", "-", "-", "проспект Волі, 47, Луцьк", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Дніпропетровська область",
            cities = listOf(
                City(
                    name = "Дніпро",
                    centers = listOf(
                        Center("dnipro", "Дніпропетровська обласна станція переливання крові", "+38 (056) 720-90-75", "info@donor.dp.ua", "donor.dp", "donor.dp.ua", "проспект Богдана Хмельницького, 17, Дніпро", true, true, true),
                    ),
                ),
                City(
                    name = "Кривий Ріг",
                    centers = listOf(
                        Center("kryvyi-rih", "Криворізька станція переливання крові", "+38 (068) 696-90-92", "kspk53020@gmail.com", "@profile.php?id=100069140842918", "-", "вулиця Медична, 12, Кривий Ріг", true, false, true),
                    ),
                ),
                City(
                    name = "Кам’янське",
                    centers = listOf(
                        Center("kamianske", "Кам'янська міська станція переливання крові", "+38 (068) 68-78-092", "kmspk2018@gmail.com", "-", "-", "вулиця Інститутська, 6, Кам’янське", true, false, false),
                    ),
                ),
                City(
                    name = "Нікополь",
                    centers = listOf(
                        Center("nikopol", "Нікопольська міська станція переливання крові", "+38 (056) 629-13-72", "-", "-", "-", "пр. Трубників, 56, корпус 2, Нікополь", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Донецька область",
            cities = listOf(
                City(
                    name = "Краматорськ",
                    centers = listOf(
                        Center("kramatorsk", "Станція переливання крові м. Краматорська", "+38 099 297 1991", "-", "-", "-", "в процесі релокації на підконтрольну Україні територію", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Житомирська область",
            cities = listOf(
                City(
                    name = "Житомир",
                    centers = listOf(
                        Center("zhytomyr", "Житомирський обласний центр крові", "+ 38 (063) 822-19-62", "rbc.zt.ua@gmail.com", "@ztbloodcentre", "-", "вулиця Кибальчича, 16, Житомир", true, true, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Закарпатська область",
            cities = listOf(
                City(
                    name = "Берегове",
                    centers = listOf(
                        Center("berehove", "Відділення переливання крові Берегівської лікарні ім. Бертолона Ліннера", "+38 (056) 629-13-72", "-", "-", "beregove.crl.net.ua", "вул. Ліннера, 2. м. Берегове", true, false, false),
                    ),
                ),
                City(
                    name = "Іршава",
                    centers = listOf(
                        Center("irshava", "Відділення переливання крові Іршавської районної лікарні", "+38 (031) 442-24-31", "-", "-", "-", "вул. Комарова, 16, Іршава", true, false, false),
                    ),
                ),
                City(
                    name = "Міжгір'я",
                    centers = listOf(
                        Center("mizhhiria", "Відділення переливання крові Міжгірської районної лікарні", "+38 (050) 153-45-94", "-", "-", "-", "вулиця Возз'єднання, 4, Міжгір'я", true, false, false),
                    ),
                ),
                City(
                    name = "Мукачево",
                    centers = listOf(
                        Center("mukachevo-1", "Відділення переливання крові Лікарні святого Мартина", "+38 (095) 508- 07-79", "-", "-", "stmartin.clinic/viddilennia-perelyvannia-krovi", "вулиця Михайла Грушевського, 29, Мукачево", true, false, false),
                        Center("mukachevo-2", "Biopharma Plasma Мукачево", "+38 (080) 060-00-03", "media.biopharma2018@gmail.com", "-", "biopharmaplasma.ua/plasmacenters", "вулиця Мостова, 8А, Мукачево", false, false, true),
                    ),
                ),
                City(
                    name = "Рахів",
                    centers = listOf(
                        Center("rakhiv", "Відділення переливання крові Рахівської районної лікарні", "+38 (031) 322-27-23", "-", "@KNPRakhivRL", "www.knp-rakhiv-rl.com.ua", "вулиця Карпатська, 1, Рахів", true, false, true),
                    ),
                ),
                City(
                    name = "Свалява",
                    centers = listOf(
                        Center("svaliava", "Відділення переливання крові Свалявської Центральної районної лікарні", "+38 (03133) 2-46-79", "-", "-", "-", "вул. Визволення, 23, Свалява", true, false, false),
                    ),
                ),
                City(
                    name = "Тячів",
                    centers = listOf(
                        Center("tiachiv", "Відділення переливання крові Тячівської міської лікарні", "+38 (03134) 2-12-40", "-", "-", "-", "вул. Жовтнева, 48, Тячів", true, false, false),
                    ),
                ),
                City(
                    name = "Ужгород",
                    centers = listOf(
                        Center("uzhhorod", "Закарпатська обласна станція переливання крові", "+38 (095) 01 77 313", "-", "@profile.php?id=100053771568173", "zospk.itmed.org", "вулиця Льва Толстого, 15, Ужгород", true, true, true),
                    ),
                ),
                City(
                    name = "Хуст",
                    centers = listOf(
                        Center("khust", "Відділення переливання крові Хустської районної лікарні", "+38 (03142) 4-22-44", "-", "-", "-", "вул. Івана Франка, 113, Хуст", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Запорізька область",
            cities = listOf(
                City(
                    name = "Запоріжжя",
                    centers = listOf(
                        Center("zaporizhzhia", "Запорізький обласний центр служби крові", "+38 (0612) 89-81-91", "info@ospk.zp.ua", "@ozspk", "ospk.zp.ua/contact", "вулиця Леоніда Жаботинського, 32, Запоріжжя", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Івано-Франківська область",
            cities = listOf(
                City(
                    name = "Івано-Франківськ",
                    centers = listOf(
                        Center("ivano-frankivsk", "Прикарпатський обласний центр служби крові", "+38 (0342) 78-90-17", "-", "@ospk.if", "ospk.if.ua", "вулиця Степана Бандери, 23, Івано-Франківськ", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Київська область",
            cities = listOf(
                City(
                    name = "Біла Церква",
                    centers = listOf(
                        Center("bila-tserkva", "Київський обласний центр служби крові", "+38(050) 616-94-34", "-", "-", "blood-center.org.ua", "вулиця Гетьмана Сагайдачного, 171, Біла Церква", true, true, true),
                    ),
                ),
                City(
                    name = "Київ",
                    centers = listOf(
                        Center("kyiv-1", "Київський міський центр крові", "+38 (044) 453-98-63", "-", "kmbckyiv.ua", "kmck.kiev.ua", "вулиця Миколи Пимоненка, 8, Київ", true, true, true),
                        Center("kyiv-2", "Національний інститут раку", "+38 (044) 293-21-35", "-", "@UkrNCI", "unci.org.ua/viddilennya-transfuziologiyi", "вулиця Юлії Здановської, 33/43, Київ", true, true, true),
                        Center("kyiv-3", "Відділення переливання крові Київського обласного центру служби крові", "+38 (044) 483-14-06", "-", "-", "http://blood-center.org.ua", "вулиця Багговутівська, 1, корпус 16, Київ", true, false, false),
                        Center("kyiv-4", "Київський міський центр крові. Структурний підрозділ на лівому березі", "+38 (093) 158 47 55", "-", "@kmbckyiv.ua", "kmck.kiev.ua", "проспект Алішера Навої, 1, Київ", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Кіровоградська область",
            cities = listOf(
                City(
                    name = "Знам'янка",
                    centers = listOf(
                        Center("znamianka", "Відділення трансфузіології Знам‘янської міської лікарні ім. А.В.Лисенка", "+38 (097) 000-55-27", "-", "-", "likarnia-znam.com.ua", "вулиця Михайла Грушевського, 15, Знам'янка", true, false, false),
                    ),
                ),
                City(
                    name = "Кропивницький",
                    centers = listOf(
                        Center("kropyvnytskyi", "Кіровоградська обласна станція переливання крові", "+38 (095) 480-03-69", "kirovograd.blood@ukr.net", "@kospkkor", "kospk.com.ua", "Преображенська вулиця, 80, Кропивницький", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Луганська область",
            cities = listOf(
                City(
                    name = "Сєверодонецьк",
                    centers = listOf(
                        Center("sieverodonetsk", "Сєверодонецька обласна станція переливання крові", "+38 050 923 75 42", "-", "-", "-", "релокована на підконтрольну Україні територію", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Львівська область",
            cities = listOf(
                City(
                    name = "Броди",
                    centers = listOf(
                        Center("brody", "Відділення переливання крові Бродівської центральної районної лікарні", "+38 (03266) 4-22-61", "-", "-", "brody.crl.net.ua", "вул. Юридика, 22, Броди", true, false, false),
                    ),
                ),
                City(
                    name = "Буськ",
                    centers = listOf(
                        Center("busk", "Відділення переливання крові Буської центральної районної лікарні", "+38 (03264) 2-11-61", "-", "@groups/2006777219455401", "buska.crl.org.ua", "вулиця Львівська, 77, Буськ", true, false, false),
                    ),
                ),
                City(
                    name = "Городок",
                    centers = listOf(
                        Center("horodok", "Відділення переливання крові Городоцької центральної лікарні", "+38 (03231) 9-11-48", "-", "-", "-", "вулиця Коцюбинського, 18, Городок", true, false, false),
                    ),
                ),
                City(
                    name = "Дрогобич",
                    centers = listOf(
                        Center("drohobych", "Дрогобицька філія Львівського обласного центру служби крові", "+38 (03244) 2-15-56", "-", "-", "-", "вулиця Солоний Ставок, 2, Дрогобич", true, false, false),
                    ),
                ),
                City(
                    name = "Жидачів",
                    centers = listOf(
                        Center("zhydachiv", "Відділення переливання крові Жидачівської центральної районної лікарні", "+38 (03239) 3-14-81", "-", "-", "zhydachiv-crl.at.ua", "вулиця Ярослава Мудрого, 29, Жидачів", true, false, false),
                    ),
                ),
                City(
                    name = "Жовква",
                    centers = listOf(
                        Center("zhovkva", "Відділення переливання крові Жовківської центральної районної лікарні", "+38 (03252) 2-11-61", "-", "-", "sites.google.com/site/crlzhovkva", "вулиця Львівська, 78, Жовква", true, false, false),
                    ),
                ),
                City(
                    name = "Золочів",
                    centers = listOf(
                        Center("zolochiv", "Відділення переливання крові Золочівської центральної районної лікарні", "+38 (03265) 4-21-36", "-", "-", "zolochiv.crl.net.ua", "вулиця Академіка Павлова, 48, Золочів", true, false, false),
                    ),
                ),
                City(
                    name = "Кам'янка-Бузька",
                    centers = listOf(
                        Center("kamianka-buzka", "Відділення переливання крові Кам'янка-Бузької центральної районної лікарні", "+38 (03254) 2-31-03", "-", "@profile.php?id=100057079194924", "-", "Перемоги вул. 29А, Кам'янка-Бузька", true, false, false),
                    ),
                ),
                City(
                    name = "Львів",
                    centers = listOf(
                        Center("lviv-1", "Львівський обласний центр служби крові", "+38 (093) 552-15-14", "-", "-", "-", "вулиця Пекарська, 65, Львів", true, true, true),
                        Center("lviv-2", "Міський центр служби крові Першого територіального медичного об’єднання м. Львова", "0 800 333 003", "-", "@1tmolviv", "www.emergency-hospital.lviv.ua", "вул. Миколайчука, 9, Львів", true, true, true),
                    ),
                ),
                City(
                    name = "Миколаїв (Львівська область)",
                    centers = listOf(
                        Center("mykolaiv-lvivska-oblast", "Відділення переливання крові Миколаївської центральної районної лікарні", "0 800 333 003", "-", "@1tmolviv", "www.emergency-hospital.lviv.ua", "вулиця Мирона Тарнавського, 1, Дроговиж", true, false, false),
                    ),
                ),
                City(
                    name = "Новий Розділ",
                    centers = listOf(
                        Center("novyi-rozdil", "Відділення переливання крові Новороздільської міської лікарні", "+38 (03261) 3-06-66", "-", "@nmllv", "-", "вул. Володимира Винниченка, 37, Новий Розділ", true, false, false),
                    ),
                ),
                City(
                    name = "Новояворівське",
                    centers = listOf(
                        Center("novoiavorivske", "Відділення переливання крові Новояворівскої районної лікарні", "+38 (03256) 4-04-71", "-", "-", "-", "вул. Тараса Шевченка, 18, Новояворівськ", true, false, false),
                    ),
                ),
                City(
                    name = "Перемишляни",
                    centers = listOf(
                        Center("peremyshliany", "Відділення переливання крові Перемишлянської центральної районної лікарні", "+38 (03263) 2-15-86", "-", "@kppcrl", "-", "вулиця Лесі Українки, Перемишляни", true, false, false),
                    ),
                ),
                City(
                    name = "Радехів",
                    centers = listOf(
                        Center("radekhiv", "Радехівська центральна районна лікарня", "+38 (068) 013-12-25", "-", "@Radekhivhospital", "-", "вул. Львівська, 8, Радехів", true, false, false),
                    ),
                ),
                City(
                    name = "Сколе",
                    centers = listOf(
                        Center("skole", "Відділення переливання крові Сколівської центральної районної лікарні", "+38 (03251) 2-11-51", "-", "@profile.php?id=100033698176124", "-", "вул. Стрийська, 19, Сколе", true, false, false),
                    ),
                ),
                City(
                    name = "Сокаль",
                    centers = listOf(
                        Center("sokal", "Сокальська філія львівського обласного центру крові", "+38 (03257) 4-62-35", "-", "-", "-", "вул. Ярослава Мудрого, 28, Сокаль", true, false, false),
                    ),
                ),
                City(
                    name = "Стрий",
                    centers = listOf(
                        Center("stryi", "Відділення переливання крові Стрийської центральної районної лікарні", "+38 (03245) 5-34-01", "-", "-", "stryi.crl.org.ua", "вул. Басараб, 15 (2 пов.), Стрий", true, false, false),
                    ),
                ),
                City(
                    name = "Червоноград",
                    centers = listOf(
                        Center("chervonohrad", "Відділення трансфузіології Червоноградської центральної міської лікарні", "+38 (03249) 2-25-15", "-", "-", "www.chervonograd-city.gov.ua/zlik.php", "вул Івасюка, 2, Червоноград", true, false, false),
                    ),
                ),
                City(
                    name = "Яворів",
                    centers = listOf(
                        Center("iavoriv", "Відділення переливання крові Яворівської центральної районної лікарні", "+38 (03259) 2-30-58", "-", "-", "-", "вул. Лозинського 4, Яворів", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Миколаївська область",
            cities = listOf(
                City(
                    name = "Миколаїв",
                    centers = listOf(
                        Center("mykolaiv", "Миколаївська обласна станція переливання крові", "+38 (0512) 36-13-27", "-", "-", "ospk.mk.ua", "пров. Радіо, 2, Миколаїв", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Одеська область",
            cities = listOf(
                City(
                    name = "Балта",
                    centers = listOf(
                        Center("balta", "Відділення переливання крові Балтської центральної районної лікарні", "+38 (04866) 2-12-01", "-", "@likarnyabalta", "-", "вул. Ломоносова , 181 , Балта ", true, false, false),
                    ),
                ),
                City(
                    name = "Білгород-Дністровський",
                    centers = listOf(
                        Center("bilhorod-dnistrovskyi", "Відділення переливання крові Білгород-Дністровської центральної районної лікарні", "+38 (04849) 6-73-82", "-", "-", "bdmbl.lic.org.ua", "вул. Радянська, 4 , Білгород-Дністровський", true, false, false),
                    ),
                ),
                City(
                    name = "Біляївка",
                    centers = listOf(
                        Center("biliaivka", "Відділення переливання крові Біляївської центральної районної лікарні", "+38 (04852) 2-52-35", "-", "-", "bil.crl.org.ua", "вул. Московська , 32 , Біляївка", true, false, false),
                    ),
                ),
                City(
                    name = "Болград",
                    centers = listOf(
                        Center("bolhrad", "Відділення трансфузіології Болградської центральної районної лікарні", "+38 (04846) 4-13-62", "-", "-", "bolgrad.hospital", " вул. Жовтнева, 65, Болград", true, false, false),
                    ),
                ),
                City(
                    name = "Доброслав(Комінтернівське)",
                    centers = listOf(
                        Center("dobroslav-kominternivske", "Відділення переливання крові Лиманської центральної районної лікарні", "+38 (04855) 9-17-58", "-", "-", "limanska.crl.net.ua", "вул. Грубника, 27, Доброслав", true, false, false),
                    ),
                ),
                City(
                    name = "Ізмаїл",
                    centers = listOf(
                        Center("izmail", "Відділення трансфузіології Ізмаїльської міської центральної лікарні", "+38 097 667 30 04", "-", "izmailcml.org.ua", "-", "пр. Незалежності, 68, Ізмаїл", true, false, false),
                    ),
                ),
                City(
                    name = "Одеса",
                    centers = listOf(
                        Center("odesa-1", "Одеська обласна станція переливання крові", "+38 (048) 776-04-67", "-", "@oospk", "ospk.od.ua", "пров. Бісквітний, 2/3, Одеса", true, true, true),
                        Center("odesa-2", "Відділення переливання крові Одеської обласної дитячої клінічної лікарні", "+38 (048) 705-53-00", "-", "@OODKL", "oodkl.od.ua", "вул. Академіка Воробйова, 3, Одеса", true, false, true),
                    ),
                ),
                City(
                    name = "Подільськ (Котовськ)",
                    centers = listOf(
                        Center("podilsk-kotovsk", "Відділення переливання крові Подільської Центральної районної лікарні", "+38 (04862) 7-33-62", "-", "-", "podilsk.crl.net.ua", "вул. Каштанова, 76, Подільськ", true, false, false),
                    ),
                ),
                City(
                    name = "Рені",
                    centers = listOf(
                        Center("reni", "Відділення переливання крові Ренійської центральної районної лікарні", "+38 (04840) 4-11-24", "renicrb@ukr.net", "-", "ренійська.црл.укр", "вул. Дунайська, 15, Рені", true, false, false),
                    ),
                ),
                City(
                    name = "Роздільна",
                    centers = listOf(
                        Center("rozdilna", "Відділення переливання крові Роздільнянської центральної районної лікарні", "+38 (04853) 3-19-42", "rozdilna_med@ukr.net", "фейсбук", "rozdilnjanska-crl.mozello.com", "вул. Леніна, 1, Роздільна", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Полтавська область",
            cities = listOf(
                City(
                    name = "Кременчук",
                    centers = listOf(
                        Center("kremenchuk", "Кременчуцька обласна станція переливання крові", "+38 (05366) 3-71-12", "-", "-", "-", "вул. Івана Мазепи, 8, Кременчук", true, true, true),
                    ),
                ),
                City(
                    name = "Полтава",
                    centers = listOf(
                        Center("poltava", "Полтавська обласна станція переливання крові", "+38 (0532) 56-09-63", "kppospk@gmail.com", "@OSPKpoltava", "pospk.ucoz.org", "вул. Шевченка, 23, Полтава", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Рівненська область",
            cities = listOf(
                City(
                    name = "Рівне",
                    centers = listOf(
                        Center("rivne", "Рівненський обласний центр служби крові", "+38 (0362) 63-68-89", "rivneospk@gmail.com ", "www.facebook.com/groups/1275101679922401", "rospk.communal.rv.ua", "вул. Степана Бандери, 31, Рівне", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Сумська область",
            cities = listOf(
                City(
                    name = "Суми",
                    centers = listOf(
                        Center("sumy", "Сумський обласний центр служби крові", "+38 (067) 5433051", "sumy.donor@biopharma.ua", "@sumykrov", "biopharmaplasma.ua/plasmacenter/plazmatsentr-v-sumakh", "пров. Громадянський, 2, Суми", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Тернопільська область",
            cities = listOf(
                City(
                    name = "Бережани",
                    centers = listOf(
                        Center("berezhany", "Відділення трансфузіології Бережанської центральної районної комунальної лікарні", "+38 (03548) 2-14-05", "bercrkl@gmail.com", "www.facebook.com/profile.php?id=61553367390789", "bercml.itmed.org", "вул. Степана Бандери, 21, Бережани", true, false, false),
                    ),
                ),
                City(
                    name = "Борщів",
                    centers = listOf(
                        Center("borshchiv", "Відділення трансфузіології Борщівського центральної районної комунальної лікарні", "+38 (03541) 2-16-09", "-", "www.facebook.com/profile.php?id=100068787077347", "-", "вул. Степана Бандери, 108, Борщів", true, false, false),
                    ),
                ),
                City(
                    name = "Бучач",
                    centers = listOf(
                        Center("buchach", "Відділення трансфузіології Бучацької міської лікарні", "+38 (03544) 2-49-59", "-", "@BuchachCMDH", "-", "вул. Генерала Шухевича, 48, Бучач", true, false, false),
                    ),
                ),
                City(
                    name = "Гусятин",
                    centers = listOf(
                        Center("husiatyn", "Відділення переливання крові Гусятинської комунальної лікарні", "(03557) 21501", "gus_ckrl@ukr.net", "-", "gusatyn.crl.org.ua", " вул. Б. Лепкого, 1, Гусятин", true, false, false),
                    ),
                ),
                City(
                    name = "Заліщики",
                    centers = listOf(
                        Center("zalishchyky", "Відділення трансфузіології Заліщанської центральної районної лікарні", "(03554) 21609", "zalcrl.zal.tr@ukr.net", "-", "zalishycka.crl.org.ua", "вул. Степана Бандери, 86, Заліщики", true, false, false),
                    ),
                ),
                City(
                    name = "Збараж",
                    centers = listOf(
                        Center("zbarazh", "Відділення трансфузіології Збаразької центральної районної комунальної лікарні", "+38 (03550) 21-3-32", "-", "-", "zbarazh.crl.net.ua", "вул. Павлова, 2, Збараж", true, false, false),
                    ),
                ),
                City(
                    name = "Зборів",
                    centers = listOf(
                        Center("zboriv", "Відділення трансфузіології Зборівської центральної районної лікарні", "+38 (03540) 2-10-54", "spytal@zbo.tr.ukrtel.net", "@ZborivskaCentralnaRajonnaLikarna", "-", "вул. Богдана Хмельницького, 17, Зборів", true, false, false),
                    ),
                ),
                City(
                    name = "Козова",
                    centers = listOf(
                        Center("kozova", "Відділення трансфузіології Козівської центральної районної лікарні", "+38 (03547) 2-16-09", "kozova.crkl@gmail.com", "-", "kozova.crl.net.ua", "вул. Зелена, 19, Козова", true, false, false),
                    ),
                ),
                City(
                    name = "Копичинці",
                    centers = listOf(
                        Center("kopychyntsi", "Відділення трансфузіології Копичинецької комунальної районної лікарні", "+38 (03557) 41535", "hospitalofkopychyntsi@gmail.com", "@kopychyntsihospital", "kop.lic.org.ua", "вул. Шевченка, 106, Копичинці", true, false, false),
                    ),
                ),
                City(
                    name = "Кременець",
                    centers = listOf(
                        Center("kremenets", "Відділення трансфузіології Кременецької районної комунальної лікарні", "067 364 25 87", "-", "-", "kremenets.lic.org.ua", "вул. Горбача, 1, Кременець", true, false, false),
                    ),
                ),
                City(
                    name = "Ланівці",
                    centers = listOf(
                        Center("lanivtsi", "Відділення трансфузіології Лановецької комунальної центральної районної лікарні", "+38 (03549) 2-16 09", "lan.kcrl@ukr.net", "-", "лановецька.црл.укр", "вул. Шевченка, 30, Ланівці", true, false, false),
                    ),
                ),
                City(
                    name = "Монастириська",
                    centers = listOf(
                        Center("monastyryska", "Відділення трансфузіології Монастириської центральної районної клінічної лікарні", "+38 (03555) 2-16-09", "monastrtmo@ukr.net", "www.facebook.com/people/КНП-Монастириська-міська-лікарня/100063542613714/", "monastr.crl.net.ua", "вул. Тараса Шевченка, 29, Монастириська", true, false, false),
                    ),
                ),
                City(
                    name = "Підгайці",
                    centers = listOf(
                        Center("pidhaitsi", "Відділення трансфузіології Підгаєцької центральної районної комунальної лікарні", "+38 (03542) 2-16-09", " pidglikarnya@ukr.net", "-", "підгайці.црл.укр", "вул. Т.Шевченка, 19, Підгайці", true, false, false),
                    ),
                ),
                City(
                    name = "Почаїв",
                    centers = listOf(
                        Center("pochaiv", "Відділення трансфузіології Почаївської районної комунальної лікарні", "+38 (03546) 61271", "knpprklp@gmail.com", "www.facebook.com/profile.php?id=100041638765760", "-", "вул. Возз'єднання, 19, Почаїв", true, false, false),
                    ),
                ),
                City(
                    name = "смт. Підволочиськ",
                    centers = listOf(
                        Center("smt-pidvolochysk", "Відділення трансфузіології Підволочиської комунальної центральної районної лікарні", "03543 21609", "-", "-", "-", "вул. Патріарха Мстислава, 102, Підволочиськ", true, false, false),
                    ),
                ),
                City(
                    name = "Теребовля",
                    centers = listOf(
                        Center("terebovlia", "Теребовлянська обласна станція переливання крові", "+38 (03551) 2-21-01", "terospk@ukr.net", "-", "-", "вул. Навроцького, 2, Теребовля", true, false, false),
                    ),
                ),
                City(
                    name = "Тернопіль",
                    centers = listOf(
                        Center("ternopil", "Тернопільський обласний центр служби крові", "+38 (0352) 52-52-12", "02009519@mail.gov.ua", "-", "tocsk.te.ua", "вул. Клінічна, 8, Тернопіль", true, true, true),
                    ),
                ),
                City(
                    name = "Чортків",
                    centers = listOf(
                        Center("chortkiv", "Відділення трансфузіології Чортківської центральної комунальної міської лікарні", "+380962430502", "02001185@mail.gov.ua", "-", "chhospital.te.ua", "вул. Дмитра Пігута, 31Б, Чортків", true, false, false),
                    ),
                ),
                City(
                    name = "Шумськ",
                    centers = listOf(
                        Center("shumsk", "Відділення трансфузіології Шумської центральної районної лікарні", "+38 (096) 1471729", "shumsk_crl@ukr.net", "@shumskcrl", "shumsk.crl.net.ua", "вул. Енергетична, 1, Шумськ", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Харківська область",
            cities = listOf(
                City(
                    name = "Харків",
                    centers = listOf(
                        Center("kharkiv", "Харківський обласний центр служби крові", "+380 95 570 4766", "khocsk@ukr.net", "@krovkh", "bloodservice.org.ua", "вул. Клочківська, 366, Харків", true, true, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Херсонська область",
            cities = listOf(
                City(
                    name = "Херсон",
                    centers = listOf(
                        Center("kherson", "Херсонський обласний центр служби крові", "+38 (055) 242-03-72", "-", "-", "-", "Миколаївське шосе, 7, Херсон", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Хмельницька область",
            cities = listOf(
                City(
                    name = "Ізяслав",
                    centers = listOf(
                        Center("iziaslav", "Відділення переливання крові Ізяславської міської багатопрофільної лікарні", "(03852)4-13-05", "icrl_iz@ukr.net", "-", "izyaslav.crl.org.ua", "вул. Б. Хмельницького, 47, Ізяслав", true, false, true),
                    ),
                ),
                City(
                    name = "Кам'янець-Подільський",
                    centers = listOf(
                        Center("kamianets-podilskyi", "Кам’янець-Подільська обласна станція переливаннякрові", "(03849) 9-15-98", "stblood@ukrpost.ua", "-", "-", "вул.Тімірязєва, 116, Кам'янець-Подільський", true, false, false),
                    ),
                ),
                City(
                    name = "Красилів",
                    centers = listOf(
                        Center("krasyliv", "Відділення трансфузіології Красилівської центральної районної лікарні", "(038255) 4-29-74", "-", "@profile.php?id=100057649920893&paipv=0&eav=AfbwCHQTw0bk2szmVggsYVhWdGrsADVP5479i-ZrkYS7PFEdh3xg0bGU5lf4Z1dXIRo&_rdr", "-", "вул. Грушевського, 140, Красилів", true, false, false),
                    ),
                ),
                City(
                    name = "Нетішин",
                    centers = listOf(
                        Center("netishyn", "Відділення трансфузіології Спеціалізованої Медико-санітарної частини м. Нетішин", "(067) 439 84 32", "kdpsmsch@ukr.net", "-", "smschnetishyn.ho.ua", "вул. Лісова, 1, Нетішин", true, false, false),
                    ),
                ),
                City(
                    name = "Славута",
                    centers = listOf(
                        Center("slavuta", "Пункт переливання крові Славутської Центральної районної лікарні", "(03842) 71167", "slavuta-103@ukr.net", "-", "славутська.црл.укр", "вул. Ярослава Мудрого, 29Г, Славута", true, false, false),
                    ),
                ),
                City(
                    name = "Старокостянтинів",
                    centers = listOf(
                        Center("starokostiantyniv", "Відділення переливання крові Старокостянтинівської центральної районної лікарні", "(03854) 3-22-76", "crl_st@ukr.net", "-", "stk.likarnja.org.ua", "вул. Захисників України, 47, Старокостянтинів", true, false, false),
                    ),
                ),
                City(
                    name = "Хмельницький",
                    centers = listOf(
                        Center("khmelnytskyi-1", "Хмельницький обласний центр служби крові", "(0382) 67-18-37", "krovhm@ukr.net", "-", "st-donor.lic.org.ua", "вул. Сковороди, 29, Хмельницький", true, true, true),
                        Center("khmelnytskyi-2", "Відділення трансфузіології Хмельницького міського перинатального центру", "+38 067 459 27 99", "kphmpts@gmail.com", "-", "hmpc.com.ua/otdelenie-tranfuziologii/", "вул. Хотовицького, 6, Хмельницький", true, false, true),
                        Center("khmelnytskyi-3", "Biopharma Plasma Хмельницький", "0800600003", "media.biopharma2018@gmail.com", "-", "biopharmaplasma.ua/plasmacenter/plazmacentr-v-khmelnytskyi", "вул. Прибузька, 11, Хмельницький", true, false, false),
                    ),
                ),
                City(
                    name = "Шепетівка",
                    centers = listOf(
                        Center("shepetivka", "Відділення трансфузіології Шепетівської багатопрофільної лікарні", "(03840) 4-04-29", "shecrl@meta.ua", "@profile.php?id=100069358046259", "shecrl.com.ua", "вул. Захисників України, 85, Шепетівка", true, false, true),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Черкаська область",
            cities = listOf(
                City(
                    name = "Ватутіне",
                    centers = listOf(
                        Center("vatutine", "Відділення переливання крові Ватутінської міської лікарні", "(0472) 2-05-09", "vatmisklik@gmail.com", "-", "vatytine.lic.org.ua", "вул. Чайковського 11, Ватутіно", true, false, false),
                    ),
                ),
                City(
                    name = "Городище",
                    centers = listOf(
                        Center("horodyshche", "Відділення переливання крові Городищенського районного територіального медичного об’єднання", "+380983353127", "knp.gormo@gmail.com", "-", "gorod.mo.org.ua", "вул. Гагаріна, 22, Городище", true, false, false),
                    ),
                ),
                City(
                    name = "Драбів",
                    centers = listOf(
                        Center("drabiv", "Відділення переливання крові Драбівської багатопрофільної лікарні", "+380673934776", "drabivcrl@gmail.com", "-", "drabiv.crl.org.ua", "вул. Садова, 1Б, Драбів", true, false, false),
                    ),
                ),
                City(
                    name = "Жашків",
                    centers = listOf(
                        Center("zhashkiv", "Відділення переливання крові Жашківської багатопрофільної лікарні", "6-12-57", "-", "-", "zh.crl.net.ua", "вул. Лікарняна, 19, Жашків", true, false, false),
                    ),
                ),
                City(
                    name = "Звенигородка",
                    centers = listOf(
                        Center("zvenyhorodka", "Відділення переливання крові Звенигородської багатопрофільної лікарні інтенсивного лікування", "+38 (04740) 2-17-02", "zvenigcrl@ukr.net", "-", "zvenig.crl.net.ua", "вул. Героїв Небесної Сотні, 79-В, Звенигородка", true, false, false),
                    ),
                ),
                City(
                    name = "Золотоноша",
                    centers = listOf(
                        Center("zolotonosha", "Золотоніська багатопрофільна лікарня", "(04737) 52461", "zl_crl@ukr.net", "-", "zlbl.pp.ua", "вул. Лікарняна, 2, Золотоноша", true, false, false),
                    ),
                ),
                City(
                    name = "Кам'янка",
                    centers = listOf(
                        Center("kamianka", "Відділення переливання крові Кам'янської багатопрофільної лікарні", "+38(093)224-32-95", "-", "-", "kam.crl.net.ua", "вул. Покровська, 90, Кам’янка", true, false, false),
                    ),
                ),
                City(
                    name = "Канів",
                    centers = listOf(
                        Center("kaniv", "Відділення переливання крові Канівської багатопрофільної лікарні", "04736 32007", "kaniv-crl@ukr.net", "-", "kaniv.crl.org.ua", "вул. Успенська, 15А, Канів", true, false, false),
                    ),
                ),
                City(
                    name = "Катеринопіль",
                    centers = listOf(
                        Center("katerynopil", "Катеринопільська багатопрофільна лікарня", "+380685431262", "-", "-", "kater.crl.net.ua", "вул. Комарова, 1, Катеринопіль", true, false, false),
                    ),
                ),
                City(
                    name = "Корсунь-Шевченківський",
                    centers = listOf(
                        Center("korsun-shevchenkivskyi", "Відділення переливання крові Корсунь-Шевченківської багатопрофільної лікарні", "+38 (0472) 31 69 17", "k_sh_crl@ukr.net", "@groups/999736860439749", "korsun.crl.net.ua", "вул. Ярослава Мудрого, 120, Корсунь-Шевченківський", true, false, false),
                    ),
                ),
                City(
                    name = "Лисянка",
                    centers = listOf(
                        Center("lysianka", "Відділення переливання крові Лисянської центральної районної лікарні", "+38 (04749) 6-13-52", "lisjanskacrl@gmail.com ", "-", "lisjanska.crl.net.ua", "вул. Михайла Грушевського, 51, Лисянка", true, false, false),
                    ),
                ),
                City(
                    name = "Маньківка",
                    centers = listOf(
                        Center("mankivka", "Відділення переливання крові Маньківської багатопрофільної лікарні", "+38 (04748) 6-19-35", "man_likarnya@ukr.net", "-", "mankivska.crl.net.ua", "вул. Павлова, 16, Маньківка", true, false, false),
                    ),
                ),
                City(
                    name = "Монастирище",
                    centers = listOf(
                        Center("monastyryshche", "Відділення переливання крові Монастирищенської багатопрофільної лікарні", "068 787 39 41", "crlmonastyr@gmail.com", "-", "monastyr.crl.net.ua", "вул.Соборна, 1, Монастирище", true, false, false),
                    ),
                ),
                City(
                    name = "Сміла",
                    centers = listOf(
                        Center("smila-1", "Відділення переливання крові Смілянської багатопрофільної лікарні ім. Софії Бобринської", "+38 (04733) 2-42-60", "sm_crl@ukr.net", "-", "crl.smila.com", "вул. Софіївська, 2, Сміла", true, false, false),
                        Center("smila-2", "Відділення переливання крові Смілянської міської лікарні", "(04733) 2-05-28", "sm_clinical@ukr.net", "-", "sml.smila.biz", "вул. Героїв Холодноярців, 82, Сміла", true, false, false),
                    ),
                ),
                City(
                    name = "Тальне",
                    centers = listOf(
                        Center("talne", "Відділення переливання крові Тальнівської багатопрофільної лікарні", "3-10-39", "talnecrl@gmail.com", "-", "talnivska.crl.net.ua", "вул. Небесної Сотні, 65а, Тальне", true, false, false),
                    ),
                ),
                City(
                    name = "Умань",
                    centers = listOf(
                        Center("uman", "Відділення переливання крові Уманської міської лікарні № 3", "+38 (04744) 5-27-13", "umspk@meta.ua", "-", "uman.info/ua/catalogue/company/id-149", "вул. Шевченка, 50, Умань", true, false, false),
                    ),
                ),
                City(
                    name = "Черкаси",
                    centers = listOf(
                        Center("cherkasy", "Черкаська обласна станція переливання крові", "+38 (063) 055-31-47", "-", "-", "-", "вулиця Остафія Дашковича, 40-А, Черкаси", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Чернівецька область",
            cities = listOf(
                City(
                    name = "Чернівці",
                    centers = listOf(
                        Center("chernivtsi", "Чернівецький обласний центр служби крові", "+38 (095) 498-01-55", "-", "-", "-", "вулиця Українська, 36, Чернівці", true, false, false),
                    ),
                ),
            ),
        ),
        Oblast(
            name = "Чернігівська область",
            cities = listOf(
                City(
                    name = "Чернігів",
                    centers = listOf(
                        Center("chernihiv", "Чернігівський обласний центр крові", "+38 (046) 291-60-12", "-", "-", "-", "вулиця Пирогова, 13, Чернігів", true, false, false),
                    ),
                ),
            ),
        ),
    )
}
