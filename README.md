# Freelance Job Portal

демонстрирует принципы ООП и работу с коллекциями на примере простого “портала” с пользователями, вакансиями и платформами.

## Структура проекта

- `User` — базовый абстрактный класс пользователя.
- `Employer` и `Freelancer` — наследники `User`.
- `Joblisting` — вакансия.
- `Portal` — платформа/сайт.
- `DataPool` — “пул данных” (in-memory хранилище) для `User`, `Joblisting`, `Portal`.
- `Main` — демонстрация возможностей (поиск, фильтрация, сортировка, equals/hashCode, полиморфизм).

### Инкапсуляция
- Поля сущностей закрыты (`private`), доступ идёт через `get/set`.
- Для строк используется проверка на пустые значения через `checkblank(...)` (в `User`, `Joblisting`, `Portal`), чтобы не хранить `null/""` (подставляется `"N/A"`).
- В `Freelancer` дополнительно реализована валидация рейтинга: значение ограничивается диапазоном **0..5**.

### Наследственность
- `Employer extends User`
- `Freelancer extends User`

Общие поля (`id`, `name`, `country`, `sphere`) и базовая логика вынесены в `User`, а специфичные детали добавляются в наследниках:
- у `Employer` — `Company`
- у `Freelancer` — `Rating`

### Полиморфизм
В `Main` пользователи хранятся и обрабатываются через общий тип `User`:

- `User u1 = new Freelancer(...)`
- `User u2 = new Employer(...)`

А затем вызывается общий метод `work()` — и поведение зависит от реального типа объекта (фрилансер/работодатель).

### Overrides (переопределения)
- `Employer` / `Freelancer` переопределяют:
  - `getRole()`
  - `work()`
  - `toString()`

- `Joblisting` и `Portal` переопределяют:
  - `toString()`
  - `equals()` и `hashCode()` (сравнение по `Id`)

Благодаря `equals/hashCode` корректно работает `HashSet` (в `Main` это показано на примерах: проверка уникальности объектов).

## DataPool
`DataPool` — это in-memory хранилище списков:
- `List<User> users`
- `List<Joblisting> joblistings`
- `List<Portal> portals`

Поддерживаются операции:
- добавление объектов: `addUser`, `addJoblisting`, `addPortal`
- получение списков: `getUsers`, `getJoblistings`, `getPortals`

## Поиск, фильтрация, сортировка

### Поиск
- `findUserById(int id)` — поиск пользователя по ID (используется в `Main`, ввод через `Scanner`).

### Фильтрация
- `activeJob()` — отбор **активных** вакансий (где `isActive == true`).

### Сортировка
- `sortFreelancersByRatingDescManual()` — ручная сортировка фрилансеров по рейтингу по убыванию (выбор максимума, аналог selection sort).
  - Сначала из `users` выбираются только `Freelancer`
  - Затем сортировка по `getRating()`.

Во время выполнения программа попросит:
1) ввести `id` для поиска пользователя  
2) ввести `id` пользователя, чтобы вызвать его `work()`
