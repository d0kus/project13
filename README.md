# Freelance Job domain.Portal

демонстрирует принципы ООП и работу с коллекциями на примере простого “портала” с пользователями, вакансиями и платформами.

## Структура проекта

- `domain.User` — базовый абстрактный класс пользователя.
- `domain.Employer` и `domain.Freelancer` — наследники `domain.User`.
- `domain.Joblisting` — вакансия.
- `domain.Portal` — платформа/сайт.
- `pool.DataPool` — “пул данных” (in-memory хранилище) для `domain.User`, `domain.Joblisting`, `domain.Portal`.
- `app.Main` — демонстрация возможностей (поиск, фильтрация, сортировка, equals/hashCode, полиморфизм).

### Инкапсуляция
- Поля сущностей закрыты (`private`), доступ идёт через `get/set`.
- Для строк используется проверка на пустые значения через `checkblank(...)` (в `domain.User`, `domain.Joblisting`, `domain.Portal`), чтобы не хранить `null/""` (подставляется `"N/A"`).
- В `domain.Freelancer` дополнительно реализована валидация рейтинга: значение ограничивается диапазоном **0..5**.

### Наследственность
- `domain.Employer extends domain.User`
- `domain.Freelancer extends domain.User`

Общие поля (`id`, `name`, `country`, `sphere`) и базовая логика вынесены в `domain.User`, а специфичные детали добавляются в наследниках:
- у `domain.Employer` — `Company`
- у `domain.Freelancer` — `Rating`

### Полиморфизм
В `app.Main` пользователи хранятся и обрабатываются через общий тип `domain.User`:

- `domain.User u1 = new domain.Freelancer(...)`
- `domain.User u2 = new domain.Employer(...)`

А затем вызывается общий метод `work()` — и поведение зависит от реального типа объекта (фрилансер/работодатель).

### Overrides (переопределения)
- `domain.Employer` / `domain.Freelancer` переопределяют:
  - `getRole()`
  - `work()`
  - `toString()`

- `domain.Joblisting` и `domain.Portal` переопределяют:
  - `toString()`
  - `equals()` и `hashCode()` (сравнение по `Id`)

Благодаря `equals/hashCode` корректно работает `HashSet` (в `app.Main` это показано на примерах: проверка уникальности объектов).

## pool.DataPool
`pool.DataPool` — это in-memory хранилище списков:
- `List<domain.User> users`
- `List<domain.Joblisting> joblistings`
- `List<domain.Portal> portals`

Поддерживаются операции:
- добавление объектов: `addUser`, `addJoblisting`, `addPortal`
- получение списков: `getUsers`, `getJoblistings`, `getPortals`

## Поиск, фильтрация, сортировка

### Поиск
- `findUserById(int id)` — поиск пользователя по ID (используется в `app.Main`, ввод через `Scanner`).

### Фильтрация
- `activeJob()` — отбор **активных** вакансий (где `isActive == true`).

### Сортировка
- `sortFreelancersByRatingDescManual()` — ручная сортировка фрилансеров по рейтингу по убыванию (выбор максимума, аналог selection sort).
  - Сначала из `users` выбираются только `domain.Freelancer`
  - Затем сортировка по `getRating()`.

Во время выполнения программа попросит:
1) ввести `id` для поиска пользователя  
2) ввести `id` пользователя, чтобы вызвать его `work()`
