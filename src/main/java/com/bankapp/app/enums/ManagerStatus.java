package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Статус менеджера.
 * <p>
 * ----- English -------
 * <p>
 * Manager status.
 */
public enum ManagerStatus {
    /**
     * ----- Russian ------
     * <p>
     * Менеджер активно работает в организации и выполняет свои обязанности.
     * <p>
     * ----- English -------
     * <p>
     * The manager actively works in the organization and fulfills his duties.
     */
    ACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер временно отсутствует по какой-либо причине, например, в отпуске или по болезни.
     * <p>
     * ----- English -------
     * <p>
     * The manager is temporarily absent for some reason, such as vacation or illness.
     */
    INACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер находится в отпуске, и его обязанности временно перераспределены на других сотрудников.
     * <p>
     * ----- English -------
     * <p>
     * The manager is on vacation and his responsibilities have been temporarily reassigned to other employees.
     */
    ON_LEAVE,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер занимает позицию временно, например, после ухода предыдущего менеджера и до назначения нового.
     * <p>
     * ----- English -------
     * <p>
     * A manager occupies a position temporarily, for example, after the previous manager leaves and until a new one is appointed.
     */
    INTERIM,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер больше не работает в организации, он был уволен или ушел по собственному желанию.
     * <p>
     * ----- English -------
     * <p>
     * The manager no longer works for the organization; he was fired or left of his own free will.
     */
    TERMINATED,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер, занимающий высшую позицию в организации или находящийся на самом верхнем уровне иерархии.
     * <p>
     * ----- English -------
     * <p>
     * A manager who occupies the highest position in an organization or is at the highest level of the hierarchy.
     */
    SENIOR, //
    /**
     * ----- Russian ------
     * <p>
     * Менеджер, который непосредственно руководит группой сотрудников и следит за выполнением задач.
     * <p>
     * ----- English -------
     * <p>
     * A manager who directly supervises a group of employees and monitors the completion of tasks.
     */
    SUPERVISOR,
    /**
     * ----- Russian ------
     * <p>
     * Менеджер, руководящий определенным подразделением или отделом в организации.
     * <p>
     * ----- English -------
     * <p>
     * A manager who leads a specific division or department in an organization.
     */
    DIRECTOR
}
