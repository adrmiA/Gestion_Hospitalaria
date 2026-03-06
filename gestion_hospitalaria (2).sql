-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 06, 2026 at 03:32 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion_hospitalaria`
--

-- --------------------------------------------------------

--
-- Table structure for table `administracion_medicamento`
--

CREATE TABLE `administracion_medicamento` (
  `id_administracion` int(11) NOT NULL,
  `id_expediente` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `dosis` varchar(100) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `fecha_administracion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `administracion_medicamento`
--

INSERT INTO `administracion_medicamento` (`id_administracion`, `id_expediente`, `id_producto`, `id_usuario`, `dosis`, `cantidad`, `fecha_administracion`) VALUES
(1, 4, 1, 12, '2 cada 8 horas', 2, '2026-03-05 21:32:22'),
(2, 4, 3, 12, '1 cada 12 horas', 3, '2026-03-06 08:24:24');

-- --------------------------------------------------------

--
-- Table structure for table `area_laboratorio`
--

CREATE TABLE `area_laboratorio` (
  `id_area` int(11) NOT NULL,
  `nombre_area` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `area_laboratorio`
--

INSERT INTO `area_laboratorio` (`id_area`, `nombre_area`) VALUES
(1, 'Hematología'),
(2, 'Bioquímica'),
(3, 'Inmunología'),
(4, 'Microbiología');

-- --------------------------------------------------------

--
-- Table structure for table `cama`
--

CREATE TABLE `cama` (
  `id_cama` int(11) NOT NULL,
  `numero` varchar(20) DEFAULT NULL,
  `sala` varchar(100) DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cama`
--

INSERT INTO `cama` (`id_cama`, `numero`, `sala`, `estado`) VALUES
(1, '101-A', 'Emergencias', 'Ocupada'),
(2, '101-B', 'Emergencias', 'Disponible'),
(3, '201-A', 'Cuidados Intensivos', 'Ocupada'),
(4, '305', 'Maternidad', 'Ocupada');

-- --------------------------------------------------------

--
-- Table structure for table `cita`
--

CREATE TABLE `cita` (
  `id_cita` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `id_paciente` int(11) NOT NULL,
  `id_medico` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cita`
--

INSERT INTO `cita` (`id_cita`, `fecha`, `hora`, `estado`, `id_paciente`, `id_medico`) VALUES
(1, '2026-02-25', '09:00:00', 'Completada', 1, 1),
(3, '2026-03-10', '10:00:00', 'Programada', 1, 1),
(5, '2026-03-10', '10:00:00', 'Programada', 1, 1),
(6, '2026-03-12', '08:00:00', 'Programada', 2, 3),
(7, '2026-03-17', '10:00:00', 'Programada', 3, 4),
(8, '2026-03-06', '11:30:00', 'Programada', 3, 6),
(9, '2026-03-07', '09:15:00', 'Programada', 3, 5),
(10, '2026-03-07', '12:08:00', 'Pendiente', 2, 3),
(11, '2026-03-06', '10:10:00', 'Programada', 3, 3);

-- --------------------------------------------------------

--
-- Table structure for table `consulta`
--

CREATE TABLE `consulta` (
  `id_consulta` int(11) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `diagnostico` text DEFAULT NULL,
  `id_expediente` int(11) NOT NULL,
  `id_medico` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `consulta`
--

INSERT INTO `consulta` (`id_consulta`, `fecha`, `diagnostico`, `id_expediente`, `id_medico`) VALUES
(1, '2026-02-25 21:35:02', 'Gripe común y deshidratación leve', 1, 1),
(3, '2026-03-04 08:28:16', 'Gastroenteritis Aguda', 2, 3),
(5, '2026-03-04 15:41:38', 'Fiebre alra', 2, 3),
(6, '2026-03-04 22:58:28', 'Migraña', 4, 6);

-- --------------------------------------------------------

--
-- Table structure for table `expediente`
--

CREATE TABLE `expediente` (
  `id_expediente` int(11) NOT NULL,
  `fecha_apertura` date DEFAULT NULL,
  `id_paciente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `expediente`
--

INSERT INTO `expediente` (`id_expediente`, `fecha_apertura`, `id_paciente`) VALUES
(1, '2026-02-25', 1),
(2, '2026-02-25', 2),
(3, '2026-03-04', 5),
(4, '2026-03-04', 3),
(5, '2026-03-04', 6),
(6, '2026-03-06', 7);

-- --------------------------------------------------------

--
-- Table structure for table `inventario`
--

CREATE TABLE `inventario` (
  `id_inventario` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `stock_actual` int(11) DEFAULT NULL,
  `stock_minimo` int(11) DEFAULT NULL,
  `stock_maximo` int(11) DEFAULT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  `fecha_actualizacion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventario`
--

INSERT INTO `inventario` (`id_inventario`, `id_producto`, `stock_actual`, `stock_minimo`, `stock_maximo`, `ubicacion`, `fecha_actualizacion`) VALUES
(1, 1, 10, 20, 500, 'Estante A-1', '2026-03-06'),
(2, 2, 150, 50, 1000, 'Bodega General', '2026-03-06'),
(3, 3, 97, 10, 200, 'Estante A-2', '2026-03-04'),
(5, 5, 20, 10, 100, 'Por asignar', '2026-03-05');

-- --------------------------------------------------------

--
-- Table structure for table `medico`
--

CREATE TABLE `medico` (
  `id_medico` int(11) NOT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `id_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medico`
--

INSERT INTO `medico` (`id_medico`, `especialidad`, `id_usuario`) VALUES
(1, 'Medicina General', 2),
(3, 'Pediatría', 6),
(4, 'Ginecología', 7),
(5, 'Ortopedia', 8),
(6, 'Neurología', 9);

-- --------------------------------------------------------

--
-- Table structure for table `movimiento_inventario`
--

CREATE TABLE `movimiento_inventario` (
  `id_movimiento` int(11) NOT NULL,
  `id_inventario` int(11) NOT NULL,
  `id_proveedor` int(11) DEFAULT NULL,
  `tipo_movimiento` enum('ENTRADA','SALIDA') NOT NULL,
  `cantidad` int(11) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `movimiento_inventario`
--

INSERT INTO `movimiento_inventario` (`id_movimiento`, `id_inventario`, `id_proveedor`, `tipo_movimiento`, `cantidad`, `motivo`, `fecha`) VALUES
(1, 1, 1, 'ENTRADA', 50, 'Compra mensual', '2026-02-25 21:35:02'),
(2, 1, 2, 'ENTRADA', 10, 'compra', '2026-02-25 21:59:41'),
(3, 1, 1, 'SALIDA', 45, 'Venta', '2026-03-01 10:36:25'),
(4, 1, 1, 'SALIDA', 50, 'Venta', '2026-03-01 10:39:37'),
(5, 1, NULL, 'ENTRADA', 20, 'Compra', '2026-03-03 23:15:16'),
(6, 2, NULL, 'SALIDA', 100, 'Venta', '2026-03-03 23:15:36'),
(8, 1, NULL, 'SALIDA', 30, 'Venta', '2026-03-04 07:54:58'),
(9, 3, NULL, 'ENTRADA', 50, 'Registro inicial de stock', '2026-03-04 23:29:08'),
(10, 5, NULL, 'ENTRADA', 20, 'Registro inicial de stock', '2026-03-05 21:10:19'),
(11, 2, NULL, 'ENTRADA', 50, 'Compra', '2026-03-06 07:11:36'),
(14, 1, NULL, 'ENTRADA', 20, 'Compra', '2026-03-06 08:20:31'),
(15, 1, NULL, 'SALIDA', 13, 'Venta', '2026-03-06 08:20:55');

-- --------------------------------------------------------

--
-- Table structure for table `ocupacion`
--

CREATE TABLE `ocupacion` (
  `id_ocupacion` int(11) NOT NULL,
  `id_paciente` int(11) NOT NULL,
  `id_cama` int(11) NOT NULL,
  `fecha_ingreso` datetime NOT NULL,
  `fecha_salida` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ocupacion`
--

INSERT INTO `ocupacion` (`id_ocupacion`, `id_paciente`, `id_cama`, `fecha_ingreso`, `fecha_salida`) VALUES
(1, 2, 1, '2026-02-25 21:35:02', '2026-03-01 11:15:47'),
(2, 2, 3, '2026-03-01 17:16:50', '2026-03-03 08:45:03'),
(3, 3, 1, '2026-03-03 08:00:00', '2026-03-04 21:46:13'),
(4, 1, 3, '2026-03-04 23:26:00', '2026-03-05 10:55:56'),
(5, 2, 4, '2026-03-05 10:55:00', NULL),
(6, 3, 1, '2026-03-05 12:47:00', '2026-03-05 16:06:20'),
(7, 3, 3, '2026-03-05 22:25:17', NULL),
(8, 3, 1, '2026-03-06 08:09:00', '2026-03-06 08:09:58'),
(9, 5, 1, '2026-03-06 08:25:22', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `orden_laboratorio`
--

CREATE TABLE `orden_laboratorio` (
  `id_orden` int(11) NOT NULL,
  `id_consulta` int(11) NOT NULL,
  `id_area` int(11) NOT NULL,
  `tipo_examen` varchar(100) DEFAULT NULL,
  `fecha_solicitud` date DEFAULT NULL,
  `resultado` text DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orden_laboratorio`
--

INSERT INTO `orden_laboratorio` (`id_orden`, `id_consulta`, `id_area`, `tipo_examen`, `fecha_solicitud`, `resultado`, `estado`) VALUES
(1, 1, 1, 'Hemograma Completo', '2026-02-25', 'Pendiente', 'En Proceso'),
(2, 3, 3, 'JKDJSK', '2026-03-04', 'Alergia a la acetaminofen', 'Completado'),
(3, 3, 3, 'JKDJSK', '2026-03-04', 'No tiene nada', 'Rechazado'),
(4, 5, 4, 'LOL', '2026-03-04', 'No es jugadora de LOL', 'Completado'),
(5, 6, 1, 'Hemograma completo', '2026-03-04', 'Globulos rojos bajos, positivo a anemia', 'Completado'),
(6, 3, 3, 'LOL', '2026-03-06', 'Positivo', 'Completado'),
(7, 6, 1, 'Hemograma completo', '2026-03-06', 'Globulos rojos bajos', 'Completado');

-- --------------------------------------------------------

--
-- Table structure for table `orden_producto`
--

CREATE TABLE `orden_producto` (
  `id_orden` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `paciente`
--

CREATE TABLE `paciente` (
  `id_paciente` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dui` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `paciente`
--

INSERT INTO `paciente` (`id_paciente`, `nombre`, `apellido`, `dui`, `telefono`, `fecha_nacimiento`, `estado`) VALUES
(1, 'Juan', 'Pérez', '01234567-8', '7788-9900', '1995-05-15', 'Activo'),
(2, 'María', 'García', '06123456-8', '7122-3344', '1985-10-12', 'Hospitalizado'),
(3, 'Adriana', 'Bonilla', '12345678-9', '6963-7400', '2008-10-09', 'Inactivo'),
(5, 'Maria', 'Quezada', '12457801-2', '6998-7064', '1972-08-11', 'Hospitalizado'),
(6, 'Jorge', 'Aguirre', '24681012-4', '7777-7777', '1992-10-31', 'Activo'),
(7, 'Kadosh', 'Palacios', '23456212-8', '7777-7777', '2007-09-24', 'Activo');

-- --------------------------------------------------------

--
-- Table structure for table `producto`
--

CREATE TABLE `producto` (
  `id_producto` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo_producto` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `unidad_medida` varchar(50) DEFAULT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `producto`
--

INSERT INTO `producto` (`id_producto`, `nombre`, `tipo_producto`, `descripcion`, `unidad_medida`, `precio_unitario`, `fecha_vencimiento`, `estado`) VALUES
(1, 'Acetaminofén 500mg', 'Medicamento', 'Caja de 20 tabletas', 'Caja', 3.50, '2025-12-31', 'Activo'),
(2, 'Jeringa 5ml', 'Insumo', 'Jeringa descartable', 'Unidad', 0.25, '2026-06-15', 'Activo'),
(3, 'Amoxicilina 1g', 'Medicamento', 'Antibiótico', 'Blíster', 5.00, '2025-08-20', 'Activo'),
(5, 'Paracetamol', 'Medicamento', '', 'Blister', 2.50, '2026-04-05', 'Por vencer');

-- --------------------------------------------------------

--
-- Table structure for table `proveedor`
--

CREATE TABLE `proveedor` (
  `id_proveedor` int(11) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `proveedor`
--

INSERT INTO `proveedor` (`id_proveedor`, `nombre`, `telefono`, `correo`, `direccion`, `estado`) VALUES
(1, 'Droguería Santa Fe', '2233-4455', 'ventas@santafe.com', 'San Salvador', 'Activo'),
(2, 'Suministros Médicos S.A', '2211-0099', 'contacto@sumed.com', 'Santa Tecla', 'Activo');

-- --------------------------------------------------------

--
-- Table structure for table `receta`
--

CREATE TABLE `receta` (
  `id_receta` int(11) NOT NULL,
  `id_consulta` int(11) NOT NULL,
  `fecha_emision` datetime DEFAULT current_timestamp(),
  `duracion` varchar(100) DEFAULT NULL,
  `frecuencia` varchar(100) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Activa'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `receta`
--

INSERT INTO `receta` (`id_receta`, `id_consulta`, `fecha_emision`, `duracion`, `frecuencia`, `observaciones`, `estado`) VALUES
(1, 1, '2026-03-04 14:43:04', '5 días', 'Cada 8 horas', 'Tomar abundante agua y evitar alimentos grasos.', 'Activa'),
(2, 5, '2026-03-04 15:41:40', '7 días', '', 'Reposo', 'Activa'),
(3, 6, '2026-03-04 22:58:28', '5 dias', '', 'Reposo', 'Activa');

-- --------------------------------------------------------

--
-- Table structure for table `receta_producto`
--

CREATE TABLE `receta_producto` (
  `id_receta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `dosis` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `receta_producto`
--

INSERT INTO `receta_producto` (`id_receta`, `id_producto`, `cantidad`, `dosis`) VALUES
(1, 1, 1, '1 tableta después de cada comida'),
(2, 3, 2, '1 cada 8 horas'),
(3, 1, 1, '2 cada 8 hrs');

-- --------------------------------------------------------

--
-- Table structure for table `rol`
--

CREATE TABLE `rol` (
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rol`
--

INSERT INTO `rol` (`id_rol`, `nombre`) VALUES
(1, 'Administrador'),
(7, 'Bodega'),
(6, 'Enfermería'),
(4, 'Laboratorista'),
(2, 'Medico'),
(3, 'Recepcionista');

-- --------------------------------------------------------

--
-- Table structure for table `signos_vitales`
--

CREATE TABLE `signos_vitales` (
  `id_signos` int(11) NOT NULL,
  `id_expediente` int(11) NOT NULL,
  `frecuencia_cardiaca` varchar(20) DEFAULT NULL,
  `frecuencia_respiratoria` varchar(20) DEFAULT NULL,
  `temperatura` varchar(10) DEFAULT NULL,
  `presion_arterial` varchar(20) DEFAULT NULL,
  `saturacion_oxigeno` varchar(10) DEFAULT NULL,
  `peso` varchar(10) DEFAULT NULL,
  `talla` varchar(10) DEFAULT NULL,
  `fecha_registro` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `signos_vitales`
--

INSERT INTO `signos_vitales` (`id_signos`, `id_expediente`, `frecuencia_cardiaca`, `frecuencia_respiratoria`, `temperatura`, `presion_arterial`, `saturacion_oxigeno`, `peso`, `talla`, `fecha_registro`) VALUES
(1, 1, '72 lpm', '18 rpm', '36.5', '120/80', '98%', '70kg', '170cm', '2026-03-03 21:00:00'),
(2, 2, '85 lpm', '20 rpm', '38.2', '130/85', '95%', '66kg', '165cm', '2026-03-01 08:00:00'),
(3, 2, '78 lpm', '18 rpm', '37.4', '125/80', '97%', '65.5kg', '165cm', '2026-03-03 10:30:00'),
(4, 2, '72 lpm', '16 rpm', '36.6', '120/80', '98%', '65kg', '165cm', '2026-03-05 15:45:00'),
(5, 4, '71', '16 rpm', '35', '120/80', '98', '40', '1.50', '2026-03-05 21:01:34'),
(6, 4, '80', '17 rpm', '37|', '120/85', '45', '45', '1.55', '2026-03-06 08:23:40');

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `id_rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre`, `usuario`, `contraseña`, `id_rol`) VALUES
(2, 'Admin Sistema', 'admin', 'password123', 1),
(3, 'Dr. Ricardo Sosa', 'rsosa', 'medico123', 2),
(4, 'Ana López', 'alopez', 'recep123', 3),
(5, 'Lic. Carlos Ruiz', 'cruiz', 'lab123', 4),
(6, 'Dra. Elena Martínez', 'emartinez', 'doc123', 2),
(7, 'Dr. Fernando Gómez', 'fgomez', 'doc123', 2),
(8, 'Dra. Claudia Rivas', 'crivas', 'doc123', 2),
(9, 'Dr. Jorge Valladares', 'jvalladares', 'doc123', 2),
(10, 'Dra. Sofía Benítez', 'sbenitez', 'doc123', 2),
(11, 'Administrador General', 'admin2', 'admin123', 1),
(12, 'Enfermera Jefe', 'enfermeria1', 'enfer123', 6),
(13, 'Encargado de Bodega', 'bodega1', 'bodega123', 7),
(22, 'Dr. Jorge Aguirre', 'jaguirre', 'doc123', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `administracion_medicamento`
--
ALTER TABLE `administracion_medicamento`
  ADD PRIMARY KEY (`id_administracion`),
  ADD KEY `id_expediente` (`id_expediente`),
  ADD KEY `id_producto` (`id_producto`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indexes for table `area_laboratorio`
--
ALTER TABLE `area_laboratorio`
  ADD PRIMARY KEY (`id_area`);

--
-- Indexes for table `cama`
--
ALTER TABLE `cama`
  ADD PRIMARY KEY (`id_cama`);

--
-- Indexes for table `cita`
--
ALTER TABLE `cita`
  ADD PRIMARY KEY (`id_cita`),
  ADD KEY `id_paciente` (`id_paciente`),
  ADD KEY `id_medico` (`id_medico`);

--
-- Indexes for table `consulta`
--
ALTER TABLE `consulta`
  ADD PRIMARY KEY (`id_consulta`),
  ADD KEY `id_expediente` (`id_expediente`),
  ADD KEY `id_medico` (`id_medico`);

--
-- Indexes for table `expediente`
--
ALTER TABLE `expediente`
  ADD PRIMARY KEY (`id_expediente`),
  ADD UNIQUE KEY `id_paciente` (`id_paciente`);

--
-- Indexes for table `inventario`
--
ALTER TABLE `inventario`
  ADD PRIMARY KEY (`id_inventario`),
  ADD UNIQUE KEY `id_producto` (`id_producto`);

--
-- Indexes for table `medico`
--
ALTER TABLE `medico`
  ADD PRIMARY KEY (`id_medico`),
  ADD UNIQUE KEY `id_usuario` (`id_usuario`);

--
-- Indexes for table `movimiento_inventario`
--
ALTER TABLE `movimiento_inventario`
  ADD PRIMARY KEY (`id_movimiento`),
  ADD KEY `fk_movimiento_inventario` (`id_inventario`),
  ADD KEY `fk_movimiento_proveedor` (`id_proveedor`);

--
-- Indexes for table `ocupacion`
--
ALTER TABLE `ocupacion`
  ADD PRIMARY KEY (`id_ocupacion`),
  ADD KEY `id_paciente` (`id_paciente`),
  ADD KEY `id_cama` (`id_cama`);

--
-- Indexes for table `orden_laboratorio`
--
ALTER TABLE `orden_laboratorio`
  ADD PRIMARY KEY (`id_orden`),
  ADD KEY `id_consulta` (`id_consulta`),
  ADD KEY `id_area` (`id_area`);

--
-- Indexes for table `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD PRIMARY KEY (`id_orden`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indexes for table `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`id_paciente`),
  ADD UNIQUE KEY `dui` (`dui`);

--
-- Indexes for table `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id_producto`);

--
-- Indexes for table `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`id_proveedor`);

--
-- Indexes for table `receta`
--
ALTER TABLE `receta`
  ADD PRIMARY KEY (`id_receta`),
  ADD KEY `id_consulta` (`id_consulta`);

--
-- Indexes for table `receta_producto`
--
ALTER TABLE `receta_producto`
  ADD PRIMARY KEY (`id_receta`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indexes for table `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indexes for table `signos_vitales`
--
ALTER TABLE `signos_vitales`
  ADD PRIMARY KEY (`id_signos`),
  ADD KEY `id_expediente` (`id_expediente`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `usuario` (`usuario`),
  ADD KEY `id_rol` (`id_rol`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `administracion_medicamento`
--
ALTER TABLE `administracion_medicamento`
  MODIFY `id_administracion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `area_laboratorio`
--
ALTER TABLE `area_laboratorio`
  MODIFY `id_area` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `cama`
--
ALTER TABLE `cama`
  MODIFY `id_cama` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `cita`
--
ALTER TABLE `cita`
  MODIFY `id_cita` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `consulta`
--
ALTER TABLE `consulta`
  MODIFY `id_consulta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `expediente`
--
ALTER TABLE `expediente`
  MODIFY `id_expediente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `inventario`
--
ALTER TABLE `inventario`
  MODIFY `id_inventario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `medico`
--
ALTER TABLE `medico`
  MODIFY `id_medico` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `movimiento_inventario`
--
ALTER TABLE `movimiento_inventario`
  MODIFY `id_movimiento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `ocupacion`
--
ALTER TABLE `ocupacion`
  MODIFY `id_ocupacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `orden_laboratorio`
--
ALTER TABLE `orden_laboratorio`
  MODIFY `id_orden` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `paciente`
--
ALTER TABLE `paciente`
  MODIFY `id_paciente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `producto`
--
ALTER TABLE `producto`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `id_proveedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `receta`
--
ALTER TABLE `receta`
  MODIFY `id_receta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `rol`
--
ALTER TABLE `rol`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `signos_vitales`
--
ALTER TABLE `signos_vitales`
  MODIFY `id_signos` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `administracion_medicamento`
--
ALTER TABLE `administracion_medicamento`
  ADD CONSTRAINT `administracion_medicamento_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id_expediente`),
  ADD CONSTRAINT `administracion_medicamento_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`),
  ADD CONSTRAINT `administracion_medicamento_ibfk_3` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`);

--
-- Constraints for table `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `cita_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`),
  ADD CONSTRAINT `cita_ibfk_2` FOREIGN KEY (`id_medico`) REFERENCES `medico` (`id_medico`);

--
-- Constraints for table `consulta`
--
ALTER TABLE `consulta`
  ADD CONSTRAINT `consulta_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id_expediente`),
  ADD CONSTRAINT `consulta_ibfk_2` FOREIGN KEY (`id_medico`) REFERENCES `medico` (`id_medico`);

--
-- Constraints for table `expediente`
--
ALTER TABLE `expediente`
  ADD CONSTRAINT `expediente_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE;

--
-- Constraints for table `inventario`
--
ALTER TABLE `inventario`
  ADD CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`) ON DELETE CASCADE;

--
-- Constraints for table `medico`
--
ALTER TABLE `medico`
  ADD CONSTRAINT `medico_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE;

--
-- Constraints for table `movimiento_inventario`
--
ALTER TABLE `movimiento_inventario`
  ADD CONSTRAINT `fk_movimiento_inventario` FOREIGN KEY (`id_inventario`) REFERENCES `inventario` (`id_inventario`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_movimiento_proveedor` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedor` (`id_proveedor`) ON DELETE SET NULL;

--
-- Constraints for table `ocupacion`
--
ALTER TABLE `ocupacion`
  ADD CONSTRAINT `ocupacion_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`),
  ADD CONSTRAINT `ocupacion_ibfk_2` FOREIGN KEY (`id_cama`) REFERENCES `cama` (`id_cama`);

--
-- Constraints for table `orden_laboratorio`
--
ALTER TABLE `orden_laboratorio`
  ADD CONSTRAINT `orden_laboratorio_ibfk_1` FOREIGN KEY (`id_consulta`) REFERENCES `consulta` (`id_consulta`),
  ADD CONSTRAINT `orden_laboratorio_ibfk_2` FOREIGN KEY (`id_area`) REFERENCES `area_laboratorio` (`id_area`);

--
-- Constraints for table `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD CONSTRAINT `orden_producto_ibfk_1` FOREIGN KEY (`id_orden`) REFERENCES `orden_laboratorio` (`id_orden`) ON DELETE CASCADE,
  ADD CONSTRAINT `orden_producto_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

--
-- Constraints for table `receta`
--
ALTER TABLE `receta`
  ADD CONSTRAINT `receta_ibfk_1` FOREIGN KEY (`id_consulta`) REFERENCES `consulta` (`id_consulta`) ON DELETE CASCADE;

--
-- Constraints for table `receta_producto`
--
ALTER TABLE `receta_producto`
  ADD CONSTRAINT `receta_producto_ibfk_1` FOREIGN KEY (`id_receta`) REFERENCES `receta` (`id_receta`) ON DELETE CASCADE,
  ADD CONSTRAINT `receta_producto_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

--
-- Constraints for table `signos_vitales`
--
ALTER TABLE `signos_vitales`
  ADD CONSTRAINT `signos_vitales_ibfk_1` FOREIGN KEY (`id_expediente`) REFERENCES `expediente` (`id_expediente`) ON DELETE CASCADE;

--
-- Constraints for table `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
